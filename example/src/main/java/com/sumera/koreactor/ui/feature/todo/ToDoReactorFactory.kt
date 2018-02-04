package com.sumera.koreactor.ui.feature.todo

import com.sumera.koreactor.lib.reactor.MviReactorFactory
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import javax.inject.Inject
import javax.inject.Provider

@PerActivity
class ToDoReactorFactory @Inject constructor(
		private val reactorProvider: Provider<ToDoReactor>
) : MviReactorFactory<ToDoReactor>() {

	override val reactor: ToDoReactor
		get() = reactorProvider.get()
}