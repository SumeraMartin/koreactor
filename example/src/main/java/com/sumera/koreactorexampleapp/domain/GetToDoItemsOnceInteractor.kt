package com.sumera.koreactorexampleapp.domain

import com.sumera.koreactorexampleapp.data.ToDoItem
import com.sumera.koreactorexampleapp.domain.base.BaseObservableInteractor
import com.sumera.koreactorexampleapp.ui.feature.todo.adapter.ToDoItemWrapper
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetToDoItemsOnceInteractor @Inject constructor() : BaseObservableInteractor<List<ToDoItemWrapper>>() {

	override fun create(): Observable<List<ToDoItemWrapper>> {
		return Observable.just(createData()).delay(3, TimeUnit.SECONDS)
	}

	private fun createData() : List<ToDoItemWrapper> {
		return listOf(
				ToDoItem(id = 1, title = "Task 1"),
				ToDoItem(id = 2, title = "Task 2"),
				ToDoItem(id = 3, title = "Task 3"),
				ToDoItem(id = 4, title = "Task 4"),
				ToDoItem(id = 5, title = "Task 5"),
				ToDoItem(id = 6, title = "Task 6")
		).map { ToDoItemWrapper(toDoItem = it, isLoading = false, isCompleted = true) }
	}
}