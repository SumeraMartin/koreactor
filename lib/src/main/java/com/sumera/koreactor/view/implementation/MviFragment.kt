package com.sumera.koreactor.view.implementation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.MviReactorDelegate
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactor.view.MviBindableView
import io.reactivex.Observable

abstract class MviFragment<STATE : MviState> : Fragment(), MviBindableView<STATE> {

	abstract fun createReactor(): MviReactor<STATE>

	private val reactorDelegate = MviReactorDelegate<STATE>()

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		reactorDelegate.initialize(createReactor(), this)
		reactorDelegate.onCreate(savedInstanceState)
	}

	override fun onStart() {
		super.onStart()

		reactorDelegate.onStart()
	}

	override fun onResume() {
		super.onResume()

		reactorDelegate.onResume()
	}

	override fun onPause() {
		super.onPause()

		reactorDelegate.onPause()
	}

	override fun onStop() {
		super.onStop()

		reactorDelegate.onStop()
	}

	override fun onDestroy() {
		reactorDelegate.onDestroy(this)

		super.onDestroy()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		reactorDelegate.onSaveInstanceState(outState)

		super.onSaveInstanceState(outState)
	}

	protected fun sendAction(action: MviAction<STATE>) {
		reactorDelegate.sendAction(action)
	}

	protected fun Observable<out MviAction<STATE>>.bindToReactor() {
		reactorDelegate.bindActionObservable(this)
	}

	protected fun <VALUE> Observable<VALUE>.observeState(action: (VALUE) -> (Unit)) {
		reactorDelegate.bindStateObservable(this, action)
	}

	protected fun <VALUE> Observable<VALUE>.observeEvent(action: (VALUE) -> (Unit)) {
		reactorDelegate.bindEventsObservable(this, action)
	}

	protected fun <REACTOR : ViewModel> getReactor(factory: ViewModelProvider.Factory, viewModel: Class<REACTOR>): REACTOR {
		return ViewModelProviders.of(this, factory).get<REACTOR>(viewModel)
	}
}
