package com.sumera.koreactor.ui.feature.counter.contract

import com.sumera.koreactor.reactor.data.MviStateReducer

sealed class CounterStateReducers: MviStateReducer<CounterState>

object IncrementReducer : CounterStateReducers() {
	override fun reduce(oldState: CounterState): CounterState {
		return oldState.copy(counter = oldState.counter + 1)
	}
}

object DecrementReducer : CounterStateReducers() {
	override fun reduce(oldState: CounterState): CounterState {
		return oldState.copy(counter = oldState.counter - 1)
	}
}