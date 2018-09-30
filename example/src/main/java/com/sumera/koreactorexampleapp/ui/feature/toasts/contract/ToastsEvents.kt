package com.sumera.koreactorexampleapp.ui.feature.toasts.contract

import com.sumera.koreactor.reactor.data.DispatchedEveryTime
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.RequireStartedStateCached
import com.sumera.koreactor.reactor.data.RequireStartedStateNotCached

sealed class ToastsEvents : MviEvent<ToastsState>()

data class ShowToastEverytime(val message: String) : ToastsEvents() {
    override val eventBehaviour = DispatchedEveryTime
}

data class ShowToastOnlyVisible(val message: String) : ToastsEvents() {
    override val eventBehaviour = RequireStartedStateNotCached
}

data class ShowToastOnlyVisibleBuffered(val message: String) : ToastsEvents() {
    override val eventBehaviour = RequireStartedStateCached
}
