package com.sumera.koreactorexampleapp.data

open class ToDoItem(
		val id: Int,
		val title: String
) {

	open fun something(): String {
		return "XXX"
	}
}