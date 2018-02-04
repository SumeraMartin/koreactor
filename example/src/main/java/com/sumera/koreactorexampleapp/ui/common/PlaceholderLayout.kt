package com.sumera.koreactorexampleapp.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.jakewharton.rxbinding2.view.clicks
import com.sumera.koreactorexampleapp.R
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_placeholder_layout.view.*

class PlaceholderLayout : FrameLayout {

	private var type = INVALID

	constructor(context: Context) : super(context) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		init()
	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		init()
	}

	private fun init() {
		View.inflate(context, R.layout.view_placeholder_layout, this)

		hide()

		isClickable = true
	}

	fun show(type: Int) {
		this.type = type

		when (type) {
			LOADING -> {
				placeholderLoading.visibility = View.VISIBLE
				placeholderError.visibility = View.GONE
				placeholderEmpty.visibility = View.GONE
			}
			GENERIC_ERROR -> {
				placeholderLoading.visibility = View.GONE
				placeholderErrorText.setText(R.string.error_generic)
				placeholderError.visibility = View.VISIBLE
				placeholderEmpty.visibility = View.GONE
			}
			NETWORK_ERROR -> {
				placeholderLoading.visibility = View.GONE
				placeholderErrorText.setText(R.string.error_no_network)
				placeholderError.visibility = View.VISIBLE
				placeholderEmpty.visibility = View.GONE
			}
			EMPTY -> {
				placeholderLoading.visibility = View.GONE
				placeholderError.visibility = View.GONE
				placeholderEmpty.visibility = View.VISIBLE
			}
			INVALID -> {
				placeholderLoading.visibility = View.GONE
				placeholderError.visibility = View.GONE
				placeholderEmpty.visibility = View.GONE
			}
		}
		visibility = View.VISIBLE
	}

	fun hide() {
		visibility = View.GONE
		type = INVALID
	}

	fun retryClicks() : Observable<Unit> {
		return placeholderErrorButton.clicks()
	}

	companion object {
		const val INVALID = -1
		const val LOADING = 0
		const val GENERIC_ERROR = 1
		const val NETWORK_ERROR = 2
		const val EMPTY = 3
	}
}
