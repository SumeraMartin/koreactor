package com.sumera.koreactorexampleapp.ui.feature.infinity.adapter

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.sumera.koreactorexampleapp.R
import com.sumera.koreactorexampleapp.tools.extensions.isVisible
import kotlinx.android.synthetic.main.view_holder_infinity_footer.view.*

class InfinityFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

	companion object {
		@LayoutRes const val layoutRes = R.layout.view_holder_infinity_footer
	}

	var onErrorClickListener: (Int) -> Unit = {}

	private val error: View = view.infinityViewHolder_error
	private val loading: View = view.infinityViewHolder_loading
	private val container: ViewGroup = view.infinityViewHolder_container

	init {
		error.setOnClickListener { onErrorClickListener.invoke(adapterPosition) }
	}

	fun bind(isLoading: Boolean, isError: Boolean) {
		loading.isVisible = isLoading
		error.isVisible = isError
		container.isVisible = isLoading || isError
	}
}
