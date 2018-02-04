package com.sumera.koreactorlib.reactor

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

abstract class MviReactorFactory<out VM : ViewModel> : ViewModelProvider.Factory {

	abstract val reactor: VM

	@SuppressWarnings("unchecked")
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return reactor as T
	}
}