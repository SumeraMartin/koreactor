package com.sumera.koreactorexampleapp.ui.feature.todo.contract

import com.sumera.koreactor.reactor.data.DispatchedEveryTime
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.RequireStartedStateCached
import com.sumera.koreactor.reactor.data.RequireStartedStateNotCached

open class ToDoEvents : MviEvent<ToDoState>()

object NavigateToSomewhereElse : ToDoEvents()

data class ShowToastEverytime(val message: String) : MviEvent<ToDoState>() {
	override val eventBehaviour = DispatchedEveryTime
}

data class ShowToastOnlyVisible(val message: String) : ToDoEvents() {
	override val eventBehaviour = RequireStartedStateNotCached
}

data class ShowToastOnlyVisibleBuffered(val message: String) : ToDoEvents() {
	override val eventBehaviour = RequireStartedStateCached
}