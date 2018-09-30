package com.sumera.koreactorexampleapp.ui.feature.toasts

import com.sumera.koreactor.reactor.MviReactorFactory
import com.sumera.koreactorexampleapp.injection.PerActivity
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class ToastsReactorFactory @Inject constructor(
        private val reactorProvider: Provider<ToastsReactor>
) : MviReactorFactory<ToastsReactor>() {

    override val reactor: ToastsReactor
        get() = reactorProvider.get()
}
