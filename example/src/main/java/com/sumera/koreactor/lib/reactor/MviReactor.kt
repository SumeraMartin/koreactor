package com.sumera.koreactor.lib.reactor

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.internal.extension.cacheEventsUntilViewIsCreated
import com.sumera.koreactor.lib.internal.extension.cacheEventsUntilViewIsStarted
import com.sumera.koreactor.lib.internal.extension.throwEventsAwayIfViewIsNotStarted
import com.sumera.koreactor.lib.internal.util.DetachReactorHelper
import com.sumera.koreactor.lib.internal.util.LifecycleEventCorrectOrderValidator
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.MviStateReducer
import com.sumera.koreactor.lib.reactor.data.either.EitherEventOrReducer
import com.sumera.koreactor.lib.reactor.data.either.fold
import com.sumera.koreactor.lib.reactor.data.event.MviEvent
import com.sumera.koreactor.lib.reactor.lifecycle.*
import com.sumera.koreactor.lib.view.MviBindable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class MviReactor<STATE : MviState> : ViewModel() {

	protected val lifecycleObservable: Observable<LifecycleEvent>
		get() = lifecycleSubject

	protected val lifecycleObservableOnce: Observable<LifecycleEvent>
		get() = lifecycleSubject.firstOrError().toObservable()

	protected val stateObservable: Observable<STATE>
		get() = stateSubject

	protected val stateObservableOnce: Observable<STATE>
		get() = stateSubject.firstOrError().toObservable()

	protected abstract fun createInitialState() : STATE

	protected abstract fun bind(actions: Observable<MviAction<STATE>>)

	private var bindableView: MviBindable<STATE>? = null

	private val keepUntilDetachDisposables = CompositeDisposable()

	private val keepUntilDestroyDisposables = CompositeDisposable()

	private val keepUntilVisibleDisposables = CompositeDisposable()

	private val safeEventsSubject = PublishSubject.create<MviEvent<STATE>>()

	private val unsafeEventsSubject = PublishSubject.create<MviEvent<STATE>>()

	private val stateSubject = BehaviorSubject.create<STATE>()

	private val actionsSubject = PublishSubject.create<MviAction<STATE>>()

	private val lifecycleSubject = BehaviorSubject.create<LifecycleEvent>()

	fun propagateAction(action: MviAction<STATE>) {
		actionsSubject.onNext(action)
	}

	fun bindWith(bindableView: MviBindable<STATE>) {
		this.bindableView = bindableView
	}

	fun onCreate(savedInstanceState: Bundle?) {
		if (savedInstanceState == null) {
			bindUnsafeEventsToSafeEvents()
			bindActionsToReactor()

			lifecycleEventChanged(AttachEvent)
		}

		bindSafeEventsToView()

		lifecycleEventChanged(CreateEvent)
	}

	fun onStart() {
		bindStateToView()

		lifecycleEventChanged(StartEvent)
	}

	fun onResume() {
		lifecycleEventChanged(ResumeEvent)
	}

	fun onPause() {
		lifecycleEventChanged(PauseEvent)
	}

	fun onStop() {
		lifecycleEventChanged(StopEvent)

		keepUntilVisibleDisposables.clear()
	}

	fun onDestroy(fragment: Fragment) {
		onDestroy(DetachReactorHelper.shouldDetachReactor(fragment))
	}

	fun onDestroy(activity: Activity) {
		onDestroy(DetachReactorHelper.shouldDetachReactor(activity))
	}

	private fun onDestroy(shouldUnbindReactor: Boolean) {
		lifecycleEventChanged(DestroyEvent)

		keepUntilDestroyDisposables.clear()

		if (shouldUnbindReactor) {
			lifecycleEventChanged(DetachEvent)
			keepUntilDetachDisposables.clear()
		}

		this.bindableView 	= null
	}

	private fun bindActionsToReactor() {
		stateSubject.onNext(createInitialState())

		val actions = actionsSubject.doOnNext(debugLog).publish()
		bind(actions)
		keepUntilDetachDisposables += actions.connect()
	}

	private fun bindStateToView() {
		val localBindable = bindableView ?: throw IllegalStateException("initialize was not called")

		val stateObservable = stateSubject.doOnNext(debugLog).publish()
		localBindable.bindToState(stateObservable)
		keepUntilVisibleDisposables += stateObservable.connect()
	}

	private fun bindUnsafeEventsToSafeEvents() {
		val loggedEventsSubject = unsafeEventsSubject.doOnNext(debugLog)

		loggedEventsSubject
				.filter { it.eventBehaviour().isRequiredViewVisibility && !it.eventBehaviour().isBuffered }
				.throwEventsAwayIfViewIsNotStarted(lifecycleObservable)
				.bindToSafeEvents()

		loggedEventsSubject
				.filter { it.eventBehaviour().isRequiredViewVisibility && it.eventBehaviour().isBuffered }
				.cacheEventsUntilViewIsStarted(lifecycleObservable)
				.bindToSafeEvents()

		loggedEventsSubject
				.filter { !it.eventBehaviour().isRequiredViewVisibility }
				.bindToSafeEvents()
	}

	private fun bindSafeEventsToView() {
		val localBindable = bindableView ?: throw IllegalStateException("initialize was not called")

		val eventsObservable = safeEventsSubject
				.cacheEventsUntilViewIsCreated(lifecycleObservable)
				.publish()

		localBindable.bindToEvent(eventsObservable)
		keepUntilDestroyDisposables += eventsObservable.connect()
	}

	inline fun <reified R : MviAction<STATE>> Observable<out MviAction<STATE>>.ofActionType(): Observable<R> = ofType(R::class.java)

	protected fun Observable<out EitherEventOrReducer<STATE>>.bindToView() {
		keepUntilDetachDisposables += observeOn(AndroidSchedulers.mainThread())
				.subscribe({ either ->
					applyEither(either)
				}, throwUnexpectedStreamError)
	}

	protected fun MviBehaviour<STATE>.bindToView() {
		keepUntilDetachDisposables += createObservable()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({ either ->
					applyEither(either)
				}, throwUnexpectedStreamError)
	}

	protected fun <T> Observable<out T>.bindToAction(action: (T) -> (Unit) = {}) {
		keepUntilDetachDisposables += observeOn(AndroidSchedulers.mainThread())
				.subscribe({ value ->
					action.invoke(value)
				}, throwUnexpectedStreamError)
	}

	private fun Observable<out MviEvent<STATE>>.bindToSafeEvents() {
		keepUntilDetachDisposables += observeOn(AndroidSchedulers.mainThread())
				.subscribe({ event ->
					safeEventsSubject.onNext(event)
				}, throwUnexpectedStreamError)
	}

	private fun applyEither(either: EitherEventOrReducer<STATE>) {
		either.toEither.fold({ applyEvent(it) }, { applyReducer(it) })
	}

	private fun applyReducer(reducer: MviStateReducer<STATE>) {
		val newState = reducer.reduce(stateSubject.value)
		stateSubject.onNext(newState)
	}

	private fun applyEvent(event: MviEvent<STATE>) {
		unsafeEventsSubject.onNext(event)
	}

	private fun lifecycleEventChanged(new: LifecycleEvent) {
		val previous = lifecycleSubject.value
		if (LifecycleEventCorrectOrderValidator.isValidOrder(previous, new)) {
			lifecycleSubject.onNext(new)
		} else {
			throw IllegalStateException("Lifecycle events are not in correct order. Make sure that you implemented it correctly. " +
					"Previous: $previous Current: $new")
		}
	}

	private val throwUnexpectedStreamError: (e: Throwable) -> Unit = {
		throw IllegalStateException("This stream should not contains any onError calls", it)
	}

	private val debugLog: (e: Any) -> Unit = {
		Log.d(this::class.java.simpleName, it.toString())
	}
}
