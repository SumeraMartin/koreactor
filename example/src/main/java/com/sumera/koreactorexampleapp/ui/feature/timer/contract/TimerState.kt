package com.sumera.koreactorexampleapp.ui.feature.timer.contract

import com.sumera.koreactor.reactor.data.MviState

data class TimerState(
        val timerValue: Int
): MviState
