package com.sumera.koreactorexampleapp.ui.feature.todo.contract

import com.sumera.koreactor.reactor.data.EveryTimeEventBehaviour
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviEventBehaviour
import com.sumera.koreactor.reactor.data.VisibleBufferedEventBehaviour
import com.sumera.koreactor.reactor.data.VisibleNonBufferedEventBehaviour

sealed class ToDoEvents : MviEvent<ToDoState>

object NavigateToSomewhereElse : ToDoEvents()

data class ShowToastEverytime(val message: String) : ToDoEvents() {
	override fun eventBehaviour(): MviEventBehaviour {
		return EveryTimeEventBehaviour
	}
}

data class ShowToastOnlyVisible(val message: String) : ToDoEvents() {
	override fun eventBehaviour(): MviEventBehaviour {
		return VisibleNonBufferedEventBehaviour
	}
}

data class ShowToastOnlyVisibleBuffered(val message: String) : ToDoEvents() {
	override fun eventBehaviour(): MviEventBehaviour {
		return VisibleBufferedEventBehaviour
	}
}

