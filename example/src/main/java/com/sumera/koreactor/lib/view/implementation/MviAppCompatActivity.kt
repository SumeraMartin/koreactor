package com.sumera.koreactor.lib.view.implementation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.view.MviBindableView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

abstract class MviAppCompatActivity<STATE : MviState> : AppCompatActivity(), MviBindableView<STATE> {

	abstract protected val reactor: MviReactor<STATE>

	private val stateDisposables = CompositeDisposable()

	private val eventDisposables = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		reactor.setBindableView(this)
		reactor.onCreate(savedInstanceState == null)
	}

	override fun onStart() {
		super.onStart()

		reactor.onStart()
	}

	override fun onResume() {
		super.onResume()

		reactor.onResume()
	}

	override fun onPause() {
		super.onPause()

		reactor.onPause()
	}

	override fun onStop() {
		super.onStop()

		reactor.onStop()
	}

	override fun onDestroy() {
		reactor.onDestroy(this)

		super.onDestroy()
	}

	override fun unbindFromState() {
		stateDisposables.clear()
	}

	override fun unbindFromEvents() {
		eventDisposables.clear()
	}

	protected fun <VIEW_MODEL : ViewModel> getReactor(factory: ViewModelProvider.Factory, viewModel: Class<VIEW_MODEL>): VIEW_MODEL {
		return ViewModelProviders.of(this, factory).get<VIEW_MODEL>(viewModel)
	}

	protected fun Observable<out MviAction<STATE>>.bindToReactor() {
		reactor.bindAction(this)
	}

	protected fun <VALUE> Observable<VALUE>.observeState(action: (VALUE) -> (Unit)) {
		stateDisposables += subscribe { value -> action(value) }
	}

	protected fun <VALUE> Observable<VALUE>.observeEvent(action: (VALUE) -> (Unit)) {
		eventDisposables += subscribe { value -> action(value) }
	}
}
