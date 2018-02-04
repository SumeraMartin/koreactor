package com.sumera.koreactor.ui.feature.main

import com.sumera.koreactor.reactor.MviReactorFactory
import com.sumera.koreactor.injection.PerActivity
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class MainReactorFactory @Inject constructor(
		private val reactorProvider: Provider<MainReactor>
) : MviReactorFactory<MainReactor>() {

	override val reactor: MainReactor
		get() = reactorProvider.get()
}