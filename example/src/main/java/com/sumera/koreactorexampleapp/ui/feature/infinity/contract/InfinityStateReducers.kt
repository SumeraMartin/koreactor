package com.sumera.koreactorexampleapp.ui.feature.infinity.contract

import com.sumera.koreactorexampleapp.data.ToDoItem
import com.sumera.koreactor.reactor.data.MviStateReducer

sealed class InfinityStateReducer : MviStateReducer<InfinityState>

data class AddNewData(val newData: List<ToDoItem>) : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		val data = oldState.data ?: listOf()
		return oldState.copy(
				isInitialLoading = false,
				isInitialError = false,
				data = data.plus(newData),
				isInfinityError = false,
				isInfinityLoading = false)
	}
}

object ShowInitialLoading : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		return oldState.copy(
				isInitialLoading = true,
				isInitialError = false,
				data = null)
	}
}

object HideInitialLoading : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		return oldState.copy(
				isInitialLoading = false,
				isInitialError = true,
				data = null)
	}
}

object ShowInitialError : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		return oldState.copy(
				isInitialLoading = false,
				isInitialError = true,
				data = null)	}
}

object ShowInfinityLoading : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		return oldState.copy(
				isInfinityError = false,
				isInfinityLoading = true)
	}
}

object ShowInfinityError : InfinityStateReducer() {
	override fun reduce(oldState: InfinityState): InfinityState {
		return oldState.copy(
				isInfinityError = true,
				isInfinityLoading = false)
	}
}