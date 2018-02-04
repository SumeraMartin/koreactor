package com.sumera.koreactor.ui.feature.timer.contract

import com.sumera.koreactor.lib.reactor.data.MviState

data class TimerState(
        val timerValue: Int
): MviState
