package com.sumera.koreactorexampleapp.ui.feature.todo.contract

import com.sumera.koreactor.reactor.data.MviStateReducer
import com.sumera.koreactorexampleapp.data.ToDoItem
import com.sumera.koreactorexampleapp.tools.extensions.replaceWithPredicate
import com.sumera.koreactorexampleapp.ui.feature.todo.adapter.ToDoItemWrapper

sealed class ToDoStateReducer : MviStateReducer<ToDoState>

data class ShowInfoReducer(val message: String) : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		return oldState.copy(infoMessage = message)
	}
}

object HideInfoReducer : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		return oldState.copy(infoMessage = "")
	}
}

data class AddToDoItemReducer(val newItemId: Int) : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		val newToDoItem = ToDoItem(id = newItemId, title = "Title $newItemId")
		val newToDoItemWrapper = ToDoItemWrapper(toDoItem = newToDoItem, isLoading = false, isCompleted = false)
		val newData = oldState.data?.plus(newToDoItemWrapper) ?: listOf(newToDoItemWrapper)
		return oldState.copy(data = newData)
	}
}

data class ShowToDoItemLoadingReducer(val itemId: Int) : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		val newData = oldState.data?.replaceWithPredicate(
				{ it.toDoItem.id == itemId },
				{ it.copy(isLoading = true, isCompleted = false) }
		)
		return oldState.copy(data = newData)
	}
}

data class ShowToDoItemCompletedReducer(val itemId: Int) : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		val newData = oldState.data?.replaceWithPredicate(
				{ it.toDoItem.id == itemId },
				{ it.copy(isLoading = false, isCompleted = true) }
		)
		return oldState.copy(data = newData)
	}
}
