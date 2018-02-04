package com.sumera.koreactor.ui.feature.timer.contract

import com.sumera.koreactor.lib.reactor.data.MviAction

sealed class TimerAction: MviAction<TimerState>

object ResetTimerAction: TimerAction()
