package com.sumera.koreactorexampleapp.ui.feature.todo.contract

import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactorexampleapp.ui.feature.todo.adapter.ToDoItemWrapper

data class ToDoState(
		val infoMessage: String,
		val data: List<ToDoItemWrapper>?
) : MviState
