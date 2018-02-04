package com.sumera.koreactor.ui.feature.counter

import com.sumera.koreactor.injection.PerActivity
import com.sumera.koreactorlib.reactor.MviReactorFactory
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class CounterReactorFactory @Inject constructor(
		private val reactorProvider: Provider<CounterReactor>
) : MviReactorFactory<CounterReactor>() {

	override val reactor: CounterReactor
		get() = reactorProvider.get()
}
