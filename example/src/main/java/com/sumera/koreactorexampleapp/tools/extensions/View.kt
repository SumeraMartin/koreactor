package com.sumera.koreactorexampleapp.tools.extensions

import android.view.View

var View.isVisible: Boolean
	get() = visibility == View.VISIBLE
	set(value) {
		if (value) {
			visibility = View.VISIBLE
		} else {
			visibility = View.GONE
		}
	}
