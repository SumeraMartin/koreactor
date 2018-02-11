package com.sumera.koreactorexampleapp.domain

import com.sumera.koreactorexampleapp.data.ToDoItem
import com.sumera.koreactorexampleapp.domain.base.BaseSingleInteractor
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SaveToDoItemInteractor @Inject constructor() : BaseSingleInteractor<ToDoItem>() {

	lateinit var item: ToDoItem

	fun init(item: ToDoItem) : SaveToDoItemInteractor {
		this.item = item
		return this
	}

	override fun create(): Single<ToDoItem> {
		val executedItem = item
		return Single
				.timer(5, TimeUnit.SECONDS)
				.map { executedItem }
	}
}
