package com.sumera.koreactor.domain

import com.sumera.koreactor.data.ToDoItem
import com.sumera.koreactor.ui.feature.todo.adapter.ToDoItemWrapper
import cz.muni.fi.pv256.movio2.uco_461464.domain.base.BaseObservableInteractor
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