package com.sumera.koreactorexampleapp.ui.feature.todo.adapter

import android.os.Parcelable
import com.sumera.koreactorexampleapp.data.ToDoItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ToDoItemWrapper(
		val toDoItem: ToDoItem,
		val isLoading: Boolean,
		val isCompleted: Boolean
) : Parcelable {

	fun copyWithCancelLoadingState(): ToDoItemWrapper {
		if (isLoading) {
			return copy(isLoading = false)
		}
		return this
	}
}