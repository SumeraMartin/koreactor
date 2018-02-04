package com.sumera.koreactor.ui.feature.todo

import com.sumera.koreactor.injection.PerActivity
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