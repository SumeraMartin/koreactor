package com.sumera.koreactorexampleapp.ui.feature.infinity.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.sumera.koreactorexampleapp.R
import com.sumera.koreactorexampleapp.data.ToDoItem
import kotlinx.android.synthetic.main.view_holder_todo_item.view.*

class InifnityViewHolder(view: View) : RecyclerView.ViewHolder(view) {

	companion object {
		@LayoutRes const val layoutRes = R.layout.view_holder_todo_item
	}

	var onToDoItemClickListener: (Int) -> Unit = {}

	private val title: TextView = view.todoViewHolder_itemTitle

	init {
		view.setOnClickListener { onToDoItemClickListener.invoke(adapterPosition) }
	}

	fun bind(toDoItem: ToDoItem) {
		title.text = toDoItem.title
	}
}
