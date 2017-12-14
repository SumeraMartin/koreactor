package com.sumera.koreactor.ui.feature.todo.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@PerActivity
class ToDoAdapter @Inject constructor() : RecyclerView.Adapter<ToDoViewHolder>() {

	private val clickSubject = PublishSubject.create<ToDoItemWrapper>()

	private var dataInternal = listOf<ToDoItemWrapper>()

	var data = listOf<ToDoItemWrapper>()
		set(value) {
			dataInternal = value
			notifyDataSetChanged()
		}

	fun toDoItemClicks() : Observable<ToDoItemWrapper> {
		return clickSubject
	}

	override fun onBindViewHolder(holder: ToDoViewHolder?, position: Int) {
		holder?.bind(dataInternal.get(position))
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ToDoViewHolder {
		val view = LayoutInflater.from(parent!!.context).inflate(ToDoViewHolder.layoutRes, parent, false)
		val holder = ToDoViewHolder(view)

		holder.onToDoItemClickListener = { position ->
			dataInternal.get(position).let { clickSubject.onNext(it) }
		}

		return holder
	}

	override fun getItemCount(): Int {
		return dataInternal.size
	}
}