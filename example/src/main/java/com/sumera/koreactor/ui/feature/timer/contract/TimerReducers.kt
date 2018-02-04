package com.sumera.koreactor.ui.feature.timer.contract

import com.sumera.koreactorlib.reactor.data.MviStateReducer

sealed class TimerReducer: MviStateReducer<TimerState>

object IncrementCountReducer: TimerReducer() {
    override fun reduce(oldState: TimerState): TimerState {
        return oldState.copy(timerValue = oldState.timerValue + 1)
    }
}

object ResetCountReducer: TimerReducer() {
    override fun reduce(oldState: TimerState): TimerState {
        return oldState.copy(timerValue = 0)
    }
}
