package com.sumera.koreactor.ui.feature.infinity.contract

import com.sumera.koreactor.data.ToDoItem
import com.sumera.koreactorlib.reactor.data.MviAction

sealed class InfinityActions : MviAction<InfinityState>

object OnRetryInitialAction : InfinityActions()

object OnRetryInfinityLoadingAction : InfinityActions()

object OnScrolledToBottomAction : InfinityActions()

data class OnItemClickedAction(val toDoItem: ToDoItem) : InfinityActions()



