package com.sumera.koreactor.ui.feature.todo

import com.sumera.koreactorlib.reactor.MviReactorFactory
import com.sumera.koreactor.injection.PerActivity
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class ToDoReactorFactory @Inject constructor(
		private val reactorProvider: Provider<ToDoReactor>
) : MviReactorFactory<ToDoReactor>() {

	override val reactor: ToDoReactor
		get() = reactorProvider.get()
}