package com.sumera.koreactorexampleapp.ui.feature.infinity.contract

import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactorexampleapp.data.ToDoItem

data class InfinityState(
		val isInitialLoading: Boolean,
		val isInitialError: Boolean,
		val isInfinityLoading: Boolean,
		val isInfinityError: Boolean,
		val isCompleted: Boolean,
		val data: List<ToDoItem>?
) : MviState
