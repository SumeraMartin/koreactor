package com.sumera.koreactor.reactor

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.sumera.koreactor.internal.util.DetachReactorHelper
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactor.view.MviBindableView
import com.sumera.koreactor.view.MviBindableViewDelegate
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class MviReactorDelegate<STATE : MviState> : MviBindableViewDelegate<STATE> {

	private val reactor: MviReactor<STATE>
		get() = reactorUnsafe ?: throw IllegalStateException("initialize method was not called")

	private val bindableView: MviBindableView<STATE>
		get() = bindableViewUnsafe ?: throw IllegalStateException("initialize method was not called")

	private var reactorUnsafe: MviReactor<STATE>? = null

	private var bindableViewUnsafe: MviBindableView<STATE>? = null

	private val stateDisposables = CompositeDisposable()

	private val eventDisposables = CompositeDisposable()

	private val actionDisposables = CompositeDisposable()

	fun initialize(reactor: MviReactor<STATE>, bindableView: MviBindableView<STATE>) {
		this.reactorUnsafe = reactor
		this.bindableViewUnsafe = bindableView
	}

	fun onCreate(savedInstanceState: Bundle?) {
		reactor.setBindableView(this)
		reactor.onCreate(savedInstanceState == null)
	}

	fun onStart() {
		reactor.onStart()
	}

	fun onResume() {
		reactor.onResume()
	}

	fun onPause() {
		reactor.onPause()
	}

	fun onStop() {
		reactor.onStop()
	}

	fun onDestroy(fragment: Fragment) {
		reactor.onDestroy(DetachReactorHelper.shouldDetachReactor(fragment))
	}

	fun onDestroy(activity: Activity) {
		reactor.onDestroy(DetachReactorHelper.shouldDetachReactor(activity))
	}

	fun onSaveInstanceState(outState: Bundle) {
		// TODO add implementation of savedInstanceState to reactor
	}

	fun sendAction(action: MviAction<STATE>) {
		reactor.sendAction(action)
	}

	fun bindActionObservable(actionObservable: Observable<out MviAction<STATE>>) {
		actionDisposables += actionObservable.subscribe { value -> reactor.sendAction(value) }
	}

	fun <VALUE> bindStateObservable(actionObservable: Observable<VALUE>, action: (VALUE) -> (Unit)) {
		stateDisposables += actionObservable.subscribe { value -> action(value) }
	}

	fun <VALUE> bindEventsObservable(actionObservable: Observable<VALUE>, action: (VALUE) -> (Unit)) {
		eventDisposables += actionObservable.subscribe { value -> action(value) }
	}

	override fun unbindFromState() {
		stateDisposables.clear()
	}

	override fun unbindFromEvents() {
		eventDisposables.clear()
	}

	override fun unbindActions() {
		actionDisposables.clear()
	}

	override fun bindToState(stateObservable: Observable<STATE>) {
		bindableView.bindToState(stateObservable)
	}

	override fun bindToEvent(eventsObservable: Observable<MviEvent<STATE>>) {
		bindableView.bindToEvent(eventsObservable)
	}
}
