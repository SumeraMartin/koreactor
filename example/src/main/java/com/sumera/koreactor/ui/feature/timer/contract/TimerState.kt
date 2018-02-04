package com.sumera.koreactor.ui.feature.timer.contract

import com.sumera.koreactorlib.reactor.data.MviState

data class TimerState(
        val timerValue: Int
): MviState
