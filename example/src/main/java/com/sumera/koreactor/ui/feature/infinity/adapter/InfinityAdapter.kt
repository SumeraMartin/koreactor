package com.sumera.koreactor.ui.feature.infinity.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sumera.koreactor.data.ToDoItem
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@PerActivity
class InfinityAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	companion object {

		val ITEM_VIEW_TYPE = 1

		val FOOTER_VIEW_TYPE = 2
	}

	private val clickSubject = PublishSubject.create<ToDoItem>()

	private val bottomScrolledSubject = PublishSubject.create<Unit>()

	private val retryInfinityLoadingSubject = PublishSubject.create<Unit>()

	private var dataInternal = listOf<ToDoItem>()

	private var isLoading = false

	private var isError = false

	var data = listOf<ToDoItem>()
		set(value) {
			dataInternal = value
			notifyDataSetChanged()
		}

	fun setLoading(isLoading: Boolean) {
		this.isLoading = isLoading
		notifyDataSetChanged()
	}

	fun setError(isError: Boolean) {
		this.isError = isError
		notifyDataSetChanged()
	}

	fun toDoItemClicks() : Observable<ToDoItem> {
		return clickSubject
	}

	fun bottomScrooled() : Observable<Unit> {
		return bottomScrolledSubject
	}

	fun retryClicked() : Observable<Unit> {
		return retryInfinityLoadingSubject
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is InifnityViewHolder) {
			holder.bind(dataInternal.get(position))
		}

		if (holder is InfinityFooterViewHolder) {
			holder.bind(isLoading, isError)
		}

		if (position == itemCount - 1 && itemCount != 1) {
			bottomScrolledSubject.onNext(Unit)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
		val holder: RecyclerView.ViewHolder

		if (viewType == FOOTER_VIEW_TYPE) {
			val view = LayoutInflater.from(parent!!.context).inflate(InfinityFooterViewHolder.layoutRes, parent, false)
			holder = InfinityFooterViewHolder(view)
			holder.onErrorClickListener = { position ->
				retryInfinityLoadingSubject.onNext(Unit)
			}
		} else {
			val view = LayoutInflater.from(parent!!.context).inflate(InifnityViewHolder.layoutRes, parent, false)
			holder = InifnityViewHolder(view)
			holder.onToDoItemClickListener = { position ->
				dataInternal.get(position).let { clickSubject.onNext(it) }
			}
		}

		return holder
	}

	override fun getItemViewType(position: Int): Int {
		if (position == itemCount - 1) {
			return FOOTER_VIEW_TYPE
		}
		return ITEM_VIEW_TYPE
	}

	override fun getItemCount(): Int {
		return dataInternal.size + 1 // + Infinity footer
	}
}