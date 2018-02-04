package com.sumera.koreactor.ui.feature.simpleloading.contract

import com.sumera.koreactorlib.reactor.data.MviAction

sealed class SimpleLoadingActions : MviAction<SimpleLoadingState>

object RetryClicked : SimpleLoadingActions()