package com.sumera.koreactor.ui.feature.infinity.contract

import com.sumera.koreactor.data.ToDoItem
import com.sumera.koreactorlib.reactor.data.MviState

data class InfinityState(
		val isInitialLoading: Boolean,
		val isInitialError: Boolean,
		val isInfinityLoading: Boolean,
		val isInfinityError: Boolean,
		val data: List<ToDoItem>?
) : MviState
