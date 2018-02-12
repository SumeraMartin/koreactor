package com.sumera.koreactorexampleapp.ui.feature.infinity.contract

import com.sumera.koreactor.reactor.data.MviStateReducer
import com.sumera.koreactorexampleapp.data.ToDoItem

sealed class InfinityStateReducer : MviStateReducer<InfinityState>

data class AppendNewData(val newData: List<ToDoItem>) : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		val data = oldState.data ?: listOf()
		return oldState.copy(
				isInitialLoading = false,
				isInitialError = false,
				data = data.plus(newData),
				isInfinityError = false,
				isInfinityLoading = false
		)
	}
}

data class SetNewData(val newData: List<ToDoItem>) : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		return oldState.copy(
				isInitialLoading = false,
				isInitialError = false,
				data = newData,
				isInfinityError = false,
				isInfinityLoading = false
		)
	}
}


object ShowInitialLoading : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		return oldState.copy(
				isCompleted = false,
				isInitialLoading = true,
				isInitialError = false
		)
	}
}

object ShowInitialError : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		return oldState.copy(
				isInitialLoading = false,
				isInitialError = true
		)
	}
}

object ShowInfinityLoading : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		return oldState.copy(
				isInfinityError = false,
				isInfinityLoading = true
		)
	}
}

object ShowInfinityError : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		return oldState.copy(
				isInfinityError = true,
				isInfinityLoading = false
		)
	}
}

object ShowCompleted : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		return oldState.copy(isCompleted = true)
	}
}