package com.sumera.koreactorexampleapp.ui.feature.infinity.contract

import com.sumera.koreactorexampleapp.data.ToDoItem
import com.sumera.koreactor.reactor.data.MviAction

sealed class InfinityActions : MviAction<InfinityState>

object OnRetryInitialAction : InfinityActions()

object OnRetryInfinityLoadingAction : InfinityActions()

object OnScrolledToBottomAction : InfinityActions()

data class OnItemClickedAction(val toDoItem: ToDoItem) : InfinityActions()



