package com.sumera.koreactor.data

open class ToDoItem(
		val id: Int,
		val title: String
) {

	open fun something(): String {
		return "XXX"
	}
}