package com.sumera.koreactor.ui.feature.todo.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.sumera.koreactor.R
import kotlinx.android.synthetic.main.view_holder_todo_item.view.*

class ToDoViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

	companion object {
		@LayoutRes const val layoutRes = R.layout.view_holder_todo_item
	}

	var onToDoItemClickListener: (Int) -> Unit = {}

	private val title: TextView = view.todoViewHolder_itemTitle
	private val state: TextView = view.todoViewHolder_state

	init {
		view.setOnClickListener { onToDoItemClickListener.invoke(adapterPosition) }
	}

	fun bind(toDoItemWrapper: ToDoItemWrapper) {
		title.text = toDoItemWrapper.toDoItem.title

		if (toDoItemWrapper.isCompleted) {
			state.text = "COMPLETE"
		} else if (toDoItemWrapper.isLoading) {
			state.text = "LOADING"
		} else {
			state.text = ""
		}
	}
}
