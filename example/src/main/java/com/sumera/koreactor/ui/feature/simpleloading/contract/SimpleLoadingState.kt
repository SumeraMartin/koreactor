package com.sumera.koreactor.ui.feature.simpleloading.contract

import com.sumera.koreactor.lib.reactor.data.MviState

data class SimpleLoadingState(
		val isLoading: Boolean,
		val isError: Boolean,
		val data: String?
) : MviState
