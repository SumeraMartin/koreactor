package com.sumera.koreactor.ui.feature.todo.contract

import com.sumera.koreactor.lib.reactor.data.EveryTimeEventBehaviour
import com.sumera.koreactor.lib.reactor.data.MviEventBehaviour
import com.sumera.koreactor.lib.reactor.data.VisibleBufferedEventBehaviour
import com.sumera.koreactor.lib.reactor.data.VisibleNonBufferedEventBehaviour
import com.sumera.koreactor.lib.reactor.data.MviEvent

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

