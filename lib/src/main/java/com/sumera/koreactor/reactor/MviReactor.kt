package com.sumera.koreactor.reactor

import android.arch.lifecycle.ViewModel
import com.sumera.koreactor.behaviour.MviBehaviour
import com.sumera.koreactor.internal.data.EventOrReducer
import com.sumera.koreactor.internal.data.fold
import com.sumera.koreactor.internal.extension.cacheEventsUntilViewIsCreated
import com.sumera.koreactor.internal.extension.cacheEventsUntilViewIsStarted
import com.sumera.koreactor.internal.extension.throwEventsAwayIfViewIsNotStarted
import com.sumera.koreactor.internal.util.LifecycleEventCorrectOrderValidator
import com.sumera.koreactor.reactor.data.AttachState
import com.sumera.koreactor.reactor.data.CreateState
import com.sumera.koreactor.reactor.data.DestroyState
import com.sumera.koreactor.reactor.data.DetachState
import com.sumera.koreactor.reactor.data.LifecycleState
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactor.reactor.data.MviStateReducer
import com.sumera.koreactor.reactor.data.PauseState
import com.sumera.koreactor.reactor.data.ResumeState
import com.sumera.koreactor.reactor.data.StartState
import com.sumera.koreactor.reactor.data.StopState
import com.sumera.koreactor.view.MviBindableViewDelegate
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class MviReactor<STATE : MviState> : ViewModel() {

	protected val lifecycleObservable: Observable<LifecycleState>
		get() = lifecycleSubject.hide()

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
		get() = stateSubject.hide()

	protected val stateSingle: Single<STATE>
		get() = stateSubject.firstOrError()

	protected abstract fun createInitialState() : STATE

	protected abstract fun bind(actions: Observable<MviAction<STATE>>)

	private val safeBindableViewDelegate: MviBindableViewDelegate<STATE>
		get() = bindableViewDelegate ?: throw IllegalStateException("setBindableView() was not called")

	private var bindableViewDelegate: MviBindableViewDelegate<STATE>? = null

	private val keepUntilDetachDisposables = CompositeDisposable()

	private val keepUntilDestroyDisposables = CompositeDisposable()

	private val keepUntilVisibleDisposables = CompositeDisposable()

	private val safeEventsSubject = PublishSubject.create<MviEvent<STATE>>()

	private val unsafeEventsSubject = PublishSubject.create<MviEvent<STATE>>()

	private val stateSubject = BehaviorSubject.create<STATE>()

	private val actionsSubject = PublishSubject.create<MviAction<STATE>>()

	private val lifecycleSubject = BehaviorSubject.create<LifecycleState>()

	private var isNewlyCreated = true

	//region Public methods

	fun sendAction(action: MviAction<STATE>) {
		actionsSubject.onNext(action)
	}

	fun setBindableView(bindableViewDelegate: MviBindableViewDelegate<STATE>) {
		this.bindableViewDelegate = bindableViewDelegate
	}

	//endregion

	//region Lifecycle methods

	fun onCreate(ignore: Boolean = false) { // TODO Remove ignore value from this method and from tests
		if (isNewlyCreated) {
			bindUnsafeEventsToSafeEvents()
			bindActionsToReactor()
			applyState(createInitialState())
			lifecycleEventChanged(AttachState)
			isNewlyCreated = false
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
		safeBindableViewDelegate.unbindActions()
		safeBindableViewDelegate.unbindFromEvents()

		if (shouldUnbindReactor) {
			lifecycleEventChanged(DetachState)
			keepUntilDetachDisposables.clear()
		}

		this.bindableViewDelegate = null
	}

	//endregion

	//region Binding extensions

	protected fun MviBehaviour<STATE>.bindToView() {
		keepUntilDetachDisposables += createObservable()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({ reactorMessage ->
					reactorMessage.messages().forEach { applyEither(it) }
				}, throwUnexpectedStreamError)
	}

	protected fun Observable<out MviReactorMessage<STATE>>.bindToView() {
		keepUntilDetachDisposables += observeOn(AndroidSchedulers.mainThread())
				.subscribe({ reactorMessage ->
					reactorMessage.messages().forEach { applyEither(it) }
				}, throwUnexpectedStreamError)
	}

	protected fun Single<out MviReactorMessage<STATE>>.bindToView() {
		keepUntilDetachDisposables += observeOn(AndroidSchedulers.mainThread())
				.subscribe({ reactorMessage ->
					reactorMessage.messages().forEach { applyEither(it) }
				}, throwUnexpectedStreamError)
	}

	protected fun Maybe<out MviReactorMessage<STATE>>.bindToView() {
		keepUntilDetachDisposables += observeOn(AndroidSchedulers.mainThread())
				.subscribe({ reactorMessage ->
					reactorMessage.messages().forEach { applyEither(it) }
				}, throwUnexpectedStreamError)
	}

	protected fun <T> Observable<out T>.bindTo(action: (T) -> (Unit) = {}) {
		keepUntilDetachDisposables += observeOn(AndroidSchedulers.mainThread())
				.subscribe({ value ->
					action.invoke(value)
				}, throwUnexpectedStreamError)
	}

	//endregion

	//region Extension functions for filtering observables by type

	protected inline fun <reified R : MviAction<STATE>> Observable<out MviAction<STATE>>.ofActionType(): Observable<R> {
		return ofType(R::class.java)
	}

	protected inline fun <reified R> Observable<out R>.type(): Observable<R> {
		return ofType(R::class.java)
	}

	protected inline fun <reified R : LifecycleState> Observable<LifecycleState>.ofLifecycleType(): Observable<R> {
		return ofType(R::class.java)
	}

	//endregion

	//region Reactor bindings

	private fun bindActionsToReactor() {
		val actions = actionsSubject.doOnNext(debugLog).publish()
		bind(actions)
		keepUntilDetachDisposables += actions.connect()
	}

	private fun bindUnsafeEventsToSafeEvents() {
		val eventsObservable = unsafeEventsSubject.doOnNext(debugLog)
				.cacheEventsUntilViewIsCreated(lifecycleObservable)
				.publish()

		val throwAwayEvents = eventsObservable
				.filter { it.eventBehaviour.isRequiredViewVisibility && !it.eventBehaviour.isCached }
				.throwEventsAwayIfViewIsNotStarted(lifecycleObservable)

		val cacheUntilViewIsStartedEvents = eventsObservable
				.filter { it.eventBehaviour.isRequiredViewVisibility && it.eventBehaviour.isCached }
				.cacheEventsUntilViewIsStarted(lifecycleObservable)

		val everyTimeEvents = eventsObservable
				.filter { !it.eventBehaviour.isRequiredViewVisibility }

		keepUntilDetachDisposables += Observable
				.merge(throwAwayEvents, cacheUntilViewIsStartedEvents, everyTimeEvents)
				.subscribe({ event ->
					safeEventsSubject.onNext(event)
				}, throwUnexpectedStreamError)

		keepUntilDetachDisposables += eventsObservable.connect()
	}

	//endregion

	//region View bindings

	private fun bindStateToView() {
		val stateObservable = stateSubject.doOnNext(debugLog).publish()
		safeBindableViewDelegate.bindToState(stateObservable)
		keepUntilVisibleDisposables += stateObservable.connect()
	}

	private fun bindSafeEventsToView() {
		val eventsObservable = safeEventsSubject.publish()
		safeBindableViewDelegate.bindToEvent(eventsObservable)
		keepUntilDestroyDisposables += eventsObservable.connect()
	}

	//endregion

	//region Data handling

	private fun applyEither(either: EventOrReducer<STATE>) {
		either.toEither.fold({ applyEvent(it) }, { applyReducer(it) })
	}

	private fun applyReducer(reducer: MviStateReducer<STATE>) {
		val newState = reducer.reduce(stateSubject.value)
		applyState(newState)
	}

	private fun applyEvent(event: MviEvent<STATE>) {
		unsafeEventsSubject.onNext(event)
	}

	private fun applyState(state: STATE) {
		stateSubject.onNext(state)
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

	//endregion

	//region Utils

	private val throwUnexpectedStreamError: (e: Throwable) -> Unit = {
		throw IllegalStateException("This stream should not contains any onError calls", it)
	}

	private val debugLog: (e: Any) -> Unit = {
		it.toString()
//		Log.d(this::class.java.simpleName, it.toString())
	}

	//endregion
}
