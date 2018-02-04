package com.sumera.koreactorexampleapp.domain

import com.sumera.koreactorexampleapp.data.ToDoItem
import com.sumera.koreactorexampleapp.domain.base.BaseObservableInteractor
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SaveToDoItemInteractor @Inject constructor() : BaseObservableInteractor<ToDoItem>() {

	lateinit var item: ToDoItem

	fun init(item: ToDoItem) : SaveToDoItemInteractor {
		this.item = item
		return this
	}

	override fun create(): Observable<ToDoItem> {
		val executedItem = item
		return Observable
				.timer(5, TimeUnit.SECONDS)
				.map { executedItem }
	}
}
