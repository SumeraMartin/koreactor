package com.sumera.koreactorexampleapp.ui.feature.counter

import com.sumera.koreactorexampleapp.injection.PerActivity
import com.sumera.koreactor.reactor.MviReactorFactory
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class CounterReactorFactory @Inject constructor(
		private val reactorProvider: Provider<CounterReactor>
) : MviReactorFactory<CounterReactor>() {

	override val reactor: CounterReactor
		get() = reactorProvider.get()
}
