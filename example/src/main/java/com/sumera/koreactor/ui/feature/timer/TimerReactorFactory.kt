package com.sumera.koreactor.ui.feature.timer

import com.sumera.koreactor.lib.reactor.MviReactorFactory
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerFragment
import javax.inject.Inject
import javax.inject.Provider

@PerFragment
class TimerReactorFactory @Inject constructor(
        private val reactorProvider: Provider<TimerReactor>
) : MviReactorFactory<TimerReactor>() {

    override val reactor: TimerReactor
        get() = reactorProvider.get()
}
