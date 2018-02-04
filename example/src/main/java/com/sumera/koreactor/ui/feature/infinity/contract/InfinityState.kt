package com.sumera.koreactor.ui.feature.todo.contract

import com.sumera.koreactor.data.ToDoItem
import com.sumera.koreactor.reactor.data.MviState

data class InfinityState(
		val isInitialLoading: Boolean,
		val isInitialError: Boolean,
		val isInfinityLoading: Boolean,
		val isInfinityError: Boolean,
		val data: List<ToDoItem>?
) : MviState
