package com.sumera.koreactor.ui.feature.main

import com.sumera.koreactor.lib.reactor.MviReactorFactory
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class MainReactorFactory @Inject constructor(
		private val reactorProvider: Provider<MainReactor>
) : MviReactorFactory<MainReactor>() {

	override val reactor: MainReactor
		get() = reactorProvider.get()
}