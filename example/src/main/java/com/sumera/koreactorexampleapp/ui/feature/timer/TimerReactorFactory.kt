package com.sumera.koreactorexampleapp.ui.feature.timer

import com.sumera.koreactorexampleapp.injection.PerFragment
import com.sumera.koreactor.reactor.MviReactorFactory
import javax.inject.Inject
import javax.inject.Provider

@PerFragment
class TimerReactorFactory @Inject constructor(
        private val reactorProvider: Provider<TimerReactor>
) : MviReactorFactory<TimerReactor>() {

    override val reactor: TimerReactor
        get() = reactorProvider.get()
}
