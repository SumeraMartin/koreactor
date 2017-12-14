package com.sumera.koreactor.lib.view.implementation

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.view.MviBindable
import io.reactivex.disposables.CompositeDisposable

abstract class MviFragment<S : MviState> : Fragment(), MviBindable<S> {

	abstract protected val reactor: MviReactor<S>

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

	override fun onStop() {
		super.onStop()

		reactor.onStop()
	}

	override fun onDestroy() {
		disposables.clear()

		reactor.onDestroy(this)

		super.onDestroy()
	}

	protected fun <T : ViewModel> getReactor(factory: ViewModelProvider.Factory, viewModel: Class<T>) : T {
		return ViewModelProviders.of(this, factory).get<T>(viewModel)
	}
}
