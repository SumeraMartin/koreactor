package com.sumera.koreactor.ui.feature.todo.adapter

import com.sumera.koreactor.data.ToDoItem

data class ToDoItemWrapper(
		val toDoItem: ToDoItem,
		val isLoading: Boolean,
		val isCompleted: Boolean
)