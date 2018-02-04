package com.sumera.koreactor.lib.reactor

import android.arch.lifecycle.ViewModel
import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.internal.data.fold
import com.sumera.koreactor.lib.internal.extension.cacheEventsUntilViewIsCreated
import com.sumera.koreactor.lib.internal.extension.cacheEventsUntilViewIsStarted
import com.sumera.koreactor.lib.internal.extension.throwEventsAwayIfViewIsNotStarted
import com.sumera.koreactor.lib.internal.util.LifecycleEventCorrectOrderValidator
import com.sumera.koreactor.lib.reactor.data.EventOrReducer
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.lib.reactor.data.MviEvent
import com.sumera.koreactor.lib.reactor.data.MviReactorMessage
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.MviStateReducer
import com.sumera.koreactor.lib.reactor.lifecycle.AttachState
import com.sumera.koreactor.lib.reactor.lifecycle.CreateState
import com.sumera.koreactor.lib.reactor.lifecycle.DestroyState
import com.sumera.koreactor.lib.reactor.lifecycle.DetachState
import com.sumera.koreactor.lib.reactor.lifecycle.LifecycleState
import com.sumera.koreactor.lib.reactor.lifecycle.PauseState
import com.sumera.koreactor.lib.reactor.lifecycle.ResumeState
import com.sumera.koreactor.lib.reactor.lifecycle.StartState
import com.sumera.koreactor.lib.reactor.lifecycle.StopState
import com.sumera.koreactor.lib.util.extension.ofLifecycleType
import com.sumera.koreactor.lib.view.MviBindableViewDelegate
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class MviReactor<STATE : MviState> : ViewModel() {

	protected val lifecycleObservable: Observable<LifecycleState>
		get() = lifecycleSubject

	protected val attachLifecycleObservable: Observable<AttachState>
		get() = lifecycleObservable.ofLifecycleType()

	protected val createLifecycleObservable: Observable<CreateState>
		get() = lifecycleObservable.ofLifecycleType()

	protected val startLifecycleObservable: Observable<StartState>
		get() = lifecycleObservable.ofLifecycleType()

	protected val resumeLifecycleObservable: Observable<ResumeState>
		get() = lifecycleObservable.ofLifecycleType()

	protected val pauseLifecycleObservable: Observable<PauseState>
		get() = lifecycleObservable.ofLifecycleType()

	protected val stopLifecycleObservable: Observable<StopState>
		get() = lifecycleObservable.ofLifecycleType()

	protected val destroyLifecycleObservable: Observable<DestroyState>
		get() = lifecycleObservable.ofLifecycleType()

	protected val detachLifecycleObservable: Observable<DetachState>
		get() = lifecycleObservable.ofLifecycleType()

	protected val stateObservable: Observable<STATE>
		get() = stateSubject

	protected val stateSingle: Single<STATE>
		get() = stateSubject.firstOrError()

	protected abstract fun createInitialState() : STATE

	protected abstract fun bind(actions: Observable<MviAction<STATE>>)

	private var bindableViewDelegate: MviBindableViewDelegate<STATE>? = null

	private val keepUntilDetachDisposables = CompositeDisposable()

	private val keepUntilDestroyDisposables = CompositeDisposable()

	private val keepUntilVisibleDisposables = CompositeDisposable()

	private val safeEventsSubject = PublishSubject.create<MviEvent<STATE>>()

	private val unsafeEventsSubject = PublishSubject.create<MviEvent<STATE>>()

	private val stateSubject = BehaviorSubject.create<STATE>()

	private val actionsSubject = PublishSubject.create<MviAction<STATE>>()

	private val lifecycleSubject = BehaviorSubject.create<LifecycleState>()

	fun sendAction(action: MviAction<STATE>) {
		actionsSubject.onNext(action)
	}

	fun setBindableView(bindableViewDelegate: MviBindableViewDelegate<STATE>) {
		this.bindableViewDelegate = bindableViewDelegate
	}

	fun onCreate(isNewlyCreated: Boolean) {
		if (isNewlyCreated) {
			bindUnsafeEventsToSafeEvents()
			bindActionsToReactor()

			stateSubject.onNext(createInitialState())

			lifecycleEventChanged(AttachState)
		}

		bindSafeEventsToView()

		lifecycleEventChanged(CreateState)
	}

	fun onStart() {
		bindStateToView()

		lifecycleEventChanged(StartState)
	}

	fun onResume() {
		lifecycleEventChanged(ResumeState)
	}

	fun onPause() {
		lifecycleEventChanged(PauseState)
	}

	fun onStop() {
		lifecycleEventChanged(StopState)

		safeBindableViewDelegate.unbindFromState()

		keepUntilVisibleDisposables.clear()
	}

	fun onDestroy(shouldUnbindReactor: Boolean) {
		lifecycleEventChanged(DestroyState)

		keepUntilDestroyDisposables.clear()

		if (shouldUnbindReactor) {
			lifecycleEventChanged(DetachState)

			safeBindableViewDelegate.unbindActions()
			safeBindableViewDelegate.unbindFromEvents()

			keepUntilDetachDisposables.clear()
		}

		this.bindableViewDelegate = null
	}

	private fun bindActionsToReactor() {
		val actions = actionsSubject.doOnNext(debugLog).publish()
		bind(actions)
		keepUntilDetachDisposables += actions.connect()
	}

	private fun bindStateToView() {
		val stateObservable = stateSubject.doOnNext(debugLog).publish()
		safeBindableViewDelegate.bindToState(stateObservable)
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
		val eventsObservable = safeEventsSubject
				.cacheEventsUntilViewIsCreated(lifecycleObservable)
				.publish()

		safeBindableViewDelegate.bindToEvent(eventsObservable)
		keepUntilDestroyDisposables += eventsObservable.connect()
	}

	inline fun <reified R : MviAction<STATE>> Observable<out MviAction<STATE>>.ofActionType(): Observable<R> = ofType(R::class.java)

	inline fun <reified R> Observable<out R>.type(): Observable<R> = ofType(R::class.java)

	protected fun MviBehaviour<STATE>.bindToView() {
		keepUntilDetachDisposables += createObservable()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({ reactorMessage ->
					reactorMessage.getMessages().forEach { applyEither(it) }
				}, throwUnexpectedStreamError)
	}

	protected fun Observable<out MviReactorMessage<STATE>>.bindToView() {
		keepUntilDetachDisposables += observeOn(AndroidSchedulers.mainThread())
				.subscribe({ reactorMessage ->
					reactorMessage.getMessages().forEach { applyEither(it) }
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

	private fun applyEither(either: EventOrReducer<STATE>) {
		either.toEither.fold({ applyEvent(it) }, { applyReducer(it) })
	}

	private fun applyReducer(reducer: MviStateReducer<STATE>) {
		val newState = reducer.reduce(stateSubject.value)
		stateSubject.onNext(newState)
	}

	private fun applyEvent(event: MviEvent<STATE>) {
		unsafeEventsSubject.onNext(event)
	}

	private fun lifecycleEventChanged(newLifecycleState: LifecycleState) {
		val previous = lifecycleSubject.value
		if (LifecycleEventCorrectOrderValidator.isValidOrder(previous, newLifecycleState)) {
			lifecycleSubject.onNext(newLifecycleState)
		} else {
			throw IllegalStateException("Lifecycle events are not in correct order. Make sure that you implemented it correctly. " +
					"Previous: $previous Current: $newLifecycleState")
		}
	}

	private val safeBindableViewDelegate: MviBindableViewDelegate<STATE>
		get() = bindableViewDelegate ?: throw IllegalStateException("initialize was not called")

	private val throwUnexpectedStreamError: (e: Throwable) -> Unit = {
		throw IllegalStateException("This stream should not contains any onError calls", it)
	}

	private val debugLog: (e: Any) -> Unit = {
//		Log.d(this::class.java.simpleName, it.toString())
	}
}
