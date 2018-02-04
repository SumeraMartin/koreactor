package com.sumera.koreactor.ui.feature.simpleloading

import com.sumera.koreactor.lib.reactor.MviReactorFactory
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class SimpleLoadingReactorFactory @Inject constructor(
        private val reactorProvider: Provider<SimpleLoadingReactor>
) : MviReactorFactory<SimpleLoadingReactor>() {

    override val reactor: SimpleLoadingReactor
        get() = reactorProvider.get()
}
