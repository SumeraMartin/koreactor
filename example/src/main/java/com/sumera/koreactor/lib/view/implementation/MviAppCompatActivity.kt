package com.sumera.koreactor.lib.view.implementation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.view.MviBindable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

abstract class MviAppCompatActivity<STATE : MviState> :
		AppCompatActivity(), MviBindable<STATE> {

	abstract protected val reactor: MviReactor<STATE>

	private val disposables = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		reactor.bindWith(this)
		reactor.onCreate(savedInstanceState)
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

		disposables.clear()

		super.onDestroy()
	}

	protected fun <VIEW_MODEL : ViewModel> getReactor(factory: ViewModelProvider.Factory, viewModel: Class<VIEW_MODEL>): VIEW_MODEL {
		return ViewModelProviders.of(this, factory).get<VIEW_MODEL>(viewModel)
	}

	protected fun Observable<out MviAction<STATE>>.bindToReactor() {
		disposables += subscribe { value -> reactor.propagateAction(value) }
	}

	protected fun <VALUE> Observable<VALUE>.observe(action: (VALUE) -> (Unit)) {
		disposables += subscribe { value -> action(value) }
	}
}
