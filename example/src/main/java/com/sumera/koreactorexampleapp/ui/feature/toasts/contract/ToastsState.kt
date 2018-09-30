package com.sumera.koreactorexampleapp.ui.feature.toasts.contract

import com.sumera.koreactor.reactor.data.MviState

data class ToastsState(
        val areToastsActive: Boolean
) : MviState
