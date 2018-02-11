package com.sumera.koreactorexampleapp.ui.feature.main.contract

import com.sumera.koreactor.reactor.data.MviEvent

sealed class MainEvent : MviEvent<MainState>()

object NavigateToInfinityEvent : MainEvent()

object NavigateToToDoEvent : MainEvent()

object NavigateToCounterEvent : MainEvent()

object NavigateToSimpleLoadingEvent : MainEvent()

object NavigateToTimerEvent : MainEvent()
