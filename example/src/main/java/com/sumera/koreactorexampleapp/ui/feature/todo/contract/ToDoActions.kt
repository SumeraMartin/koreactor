package com.sumera.koreactorexampleapp.ui.feature.todo.contract

import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactorexampleapp.ui.feature.todo.adapter.ToDoItemWrapper

sealed class ToDoActions : MviAction<ToDoState>

object OnRetryAction : ToDoActions()

object OnSwipeRefreshAction : ToDoActions()

object OnAddItemAction : ToDoActions()

object OnToolbarIconClicked : ToDoActions()

data class OnToDoItemAction(val toDoItemWrapper: ToDoItemWrapper) : ToDoActions()



