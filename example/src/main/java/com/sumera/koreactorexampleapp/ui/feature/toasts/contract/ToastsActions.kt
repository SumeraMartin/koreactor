package com.sumera.koreactorexampleapp.ui.feature.toasts.contract

import com.sumera.koreactor.reactor.data.MviAction

sealed class ToastsActions : MviAction<ToastsState>

object OnLastToastWasReceivedAction : ToastsActions()

object OnTriggerToastsButtonClickAction : ToastsActions()
