package com.sumera.koreactor.ui.base.stateless

import com.sumera.koreactor.lib.reactor.MviReactorFactory
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class StatelessReactorFactory @Inject constructor(
        private val reactorProvider: Provider<StatelessReactor>
) : MviReactorFactory<StatelessReactor>() {

    override val reactor: StatelessReactor
        get() = reactorProvider.get()
}
