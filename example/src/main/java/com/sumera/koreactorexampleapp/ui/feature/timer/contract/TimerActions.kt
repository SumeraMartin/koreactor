package com.sumera.koreactorexampleapp.ui.feature.timer.contract

import com.sumera.koreactor.reactor.data.MviAction

sealed class TimerAction: MviAction<TimerState>

object ResetTimerAction: TimerAction()
