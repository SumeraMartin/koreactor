package com.sumera.koreactor.domain

import com.sumera.koreactor.data.ToDoItem
import cz.muni.fi.pv256.movio2.uco_461464.domain.base.BaseInteractor
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SaveToDoItemInteractor @Inject constructor() : BaseInteractor<ToDoItem>() {

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
