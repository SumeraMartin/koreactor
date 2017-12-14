package com.sumera.koreactor.ui.feature.counter

import com.sumera.koreactor.lib.reactor.MviReactorFactory
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class CounterReactorFactory @Inject constructor(
		private val viewModelProvider: Provider<CounterReactor>
) : MviReactorFactory<CounterReactor>() {

	override val viewModel: CounterReactor
		get() = viewModelProvider.get()
}
