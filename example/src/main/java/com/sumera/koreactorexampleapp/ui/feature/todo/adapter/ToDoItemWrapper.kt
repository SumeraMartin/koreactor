package com.sumera.koreactorexampleapp.ui.feature.todo.adapter

import com.sumera.koreactorexampleapp.data.ToDoItem

data class ToDoItemWrapper(
		val toDoItem: ToDoItem,
		val isLoading: Boolean,
		val isCompleted: Boolean
)