package com.sumera.koreactor.ui.feature.timer

import com.sumera.koreactor.injection.PerFragment
import com.sumera.koreactorlib.reactor.MviReactorFactory
import javax.inject.Inject
import javax.inject.Provider

@PerFragment
class TimerReactorFactory @Inject constructor(
        private val reactorProvider: Provider<TimerReactor>
) : MviReactorFactory<TimerReactor>() {

    override val reactor: TimerReactor
        get() = reactorProvider.get()
}
