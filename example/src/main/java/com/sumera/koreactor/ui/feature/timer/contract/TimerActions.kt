package com.sumera.koreactor.ui.feature.timer.contract

import com.sumera.koreactorlib.reactor.data.MviAction

sealed class TimerAction: MviAction<TimerState>

object ResetTimerAction: TimerAction()
