package com.sumera.koreactorexampleapp.ui.feature.simpleloading.contract

import com.sumera.koreactor.reactor.data.MviState

data class SimpleLoadingState(
		val isLoading: Boolean,
		val isError: Boolean,
		val data: String?
) : MviState
