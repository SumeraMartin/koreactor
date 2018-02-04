package com.sumera.koreactorexampleapp.ui.feature.simpleloading.contract

import com.sumera.koreactor.reactor.data.MviAction

sealed class SimpleLoadingActions : MviAction<SimpleLoadingState>

object RetryClicked : SimpleLoadingActions()