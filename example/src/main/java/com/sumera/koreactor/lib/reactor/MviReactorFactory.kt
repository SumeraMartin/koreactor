package com.sumera.koreactor.lib.reactor

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

abstract class MviReactorFactory<out VM : ViewModel> : ViewModelProvider.Factory {

	abstract val viewModel: VM

	@SuppressWarnings("unchecked")
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return viewModel as T
	}
}