package com.sumera.koreactor.ui.feature.todo.contract

import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactor.ui.feature.todo.adapter.ToDoItemWrapper

data class ToDoState(
		val isLoading: Boolean,
		val isSwipeLoading: Boolean,
		val isError: Boolean,
		val infoMessage: String,
		val data: List<ToDoItemWrapper>?
) : MviState
