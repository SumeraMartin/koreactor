package com.sumera.koreactorexampleapp.ui.feature.toasts.contract

import com.sumera.koreactor.reactor.data.MviStateReducer

sealed class ToastsReducers : MviStateReducer<ToastsState>

object ShowToastsActiveText : ToastsReducers() {
    override fun reduce(oldState: ToastsState): ToastsState {
        return oldState.copy(areToastsActive = true)
    }
}

object HideToastsActiveText : ToastsReducers() {
    override fun reduce(oldState: ToastsState): ToastsState {
        return oldState.copy(areToastsActive = false)
    }
}
