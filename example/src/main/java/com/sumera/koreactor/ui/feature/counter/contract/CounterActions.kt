package com.sumera.koreactor.ui.feature.counter.contract

import com.sumera.koreactor.reactor.data.MviAction

sealed class CounterActions : MviAction<CounterState>

object IncrementAction : CounterActions()

object DecrementAction : CounterActions()
