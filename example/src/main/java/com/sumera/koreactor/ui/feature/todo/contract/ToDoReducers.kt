package com.sumera.koreactor.ui.feature.todo.contract

import com.sumera.koreactor.data.ToDoItem
import com.sumera.koreactor.lib.reactor.data.MviStateReducer
import com.sumera.koreactor.tools.extensions.replaceWithPredicate
import com.sumera.koreactor.ui.feature.todo.adapter.ToDoItemWrapper

sealed class ToDoStateReducer : MviStateReducer<ToDoState>

object ShowLoading : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		return oldState.copy(isLoading = true,
				isSwipeLoading = false, isError = false, data = null)
	}
}

object ShowSwipeRefreshLoading : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		return oldState.copy(isLoading = false, isSwipeLoading = true, isError = false)
	}
}

data class ShowData(val data: List<ToDoItemWrapper>?) : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		return oldState.copy(isLoading = false, isSwipeLoading = false, isError = false, data = data)
	}
}

object ShowEmpty : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		return oldState.copy(isLoading = false, isSwipeLoading = false, isError = false, data = null)
	}
}

object ShowError : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		return oldState.copy(isLoading = false, isSwipeLoading = false, isError = true, data = null)
	}
}

data class ShowInfoMessage(val message: String) : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		return oldState.copy(infoMessage = message)
	}
}

object HideInfoMessage : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		return oldState.copy(infoMessage = "")
	}
}

data class AddToDoItem(val newItemId: Int) : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		val newToDoItem = ToDoItem(id = newItemId, title = "Title " + newItemId)
		val newToDoItemWrapper = ToDoItemWrapper(toDoItem = newToDoItem, isLoading = false, isCompleted = false)
		val newData = oldState.data?.plus(newToDoItemWrapper) ?: listOf(newToDoItemWrapper)
		return oldState.copy(data = newData)
	}
}

data class ShowToDoItemLoading(val itemId: Int) : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		val newData = oldState.data?.replaceWithPredicate(
				{ it.toDoItem.id == itemId },
				{ it.copy(isLoading = true, isCompleted = false) }
		)
		return oldState.copy(data = newData)
	}
}

data class ShowToDoItemError(val itemId: Int) : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		val newData = oldState.data?.replaceWithPredicate(
				{ it.toDoItem.id == itemId },
				{ it.copy(isLoading = false, isCompleted = false) }
		)
		return oldState.copy(data = newData)
	}
}

data class ShowToDoItemCompleted(val itemId: Int) : ToDoStateReducer() {
	override fun reduce(oldState: ToDoState): ToDoState {
		val newData = oldState.data?.replaceWithPredicate(
				{ it.toDoItem.id == itemId },
				{ it.copy(isLoading = false, isCompleted = true) }
		)
		return oldState.copy(data = newData)
	}
}
