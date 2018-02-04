package com.sumera.koreactorexampleapp.ui.feature.infinity

import com.sumera.koreactorexampleapp.injection.PerActivity
import com.sumera.koreactor.reactor.MviReactorFactory
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class InfinityReactorFactory @Inject constructor(
		private val reactorProvider: Provider<InfinityReactor>
) : MviReactorFactory<InfinityReactor>() {

	override val reactor: InfinityReactor
		get() = reactorProvider.get()
}