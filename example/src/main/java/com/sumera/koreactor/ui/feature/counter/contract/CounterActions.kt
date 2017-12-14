package com.sumera.koreactor.ui.feature.counter.contract

import com.sumera.koreactor.lib.reactor.data.MviAction

sealed class CounterActions : MviAction<CounterState>

object IncrementAction : CounterActions()

object DecrementAction : CounterActions()
