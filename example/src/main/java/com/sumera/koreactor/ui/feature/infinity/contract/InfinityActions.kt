package com.sumera.koreactor.ui.feature.infinity.contract

import com.sumera.koreactor.data.ToDoItem
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.ui.feature.todo.contract.InfinityState

sealed class InfinityActions : MviAction<InfinityState>

object OnRetryInitialAction : InfinityActions()

object OnRetryInfinityLoadingAction : InfinityActions()

object OnScrolledToBottomAction : InfinityActions()

data class OnItemClickedAction(val toDoItem: ToDoItem) : InfinityActions()



