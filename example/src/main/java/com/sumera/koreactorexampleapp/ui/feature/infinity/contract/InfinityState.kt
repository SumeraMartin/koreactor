package com.sumera.koreactorexampleapp.ui.feature.infinity.contract

import com.sumera.koreactorexampleapp.data.ToDoItem
import com.sumera.koreactor.reactor.data.MviState

data class InfinityState(
		val isInitialLoading: Boolean,
		val isInitialError: Boolean,
		val isInfinityLoading: Boolean,
		val isInfinityError: Boolean,
		val data: List<ToDoItem>?
) : MviState
