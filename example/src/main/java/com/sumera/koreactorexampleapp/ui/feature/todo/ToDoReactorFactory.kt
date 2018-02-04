package com.sumera.koreactorexampleapp.ui.feature.todo

import com.sumera.koreactor.reactor.MviReactorFactory
import com.sumera.koreactorexampleapp.injection.PerActivity
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class ToDoReactorFactory @Inject constructor(
		private val reactorProvider: Provider<ToDoReactor>
) : MviReactorFactory<ToDoReactor>() {

	override val reactor: ToDoReactor
		get() = reactorProvider.get()
}