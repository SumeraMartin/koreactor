package com.sumera.koreactor.ui.feature.main.contract

import com.sumera.koreactorlib.reactor.data.MviEvent

sealed class MainEvent : MviEvent<MainState>

object NavigateToInfinityEvent : MainEvent()

object NavigateToToDoEvent : MainEvent()

object NavigateToCounterEvent : MainEvent()

object NavigateToSimpleLoadingEvent : MainEvent()

object NavigateToTimerEvent : MainEvent()
