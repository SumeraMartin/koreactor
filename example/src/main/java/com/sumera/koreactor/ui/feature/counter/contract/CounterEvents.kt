package com.sumera.koreactor.ui.feature.counter.contract

import com.sumera.koreactorlib.reactor.data.MviEvent

sealed class CounterEvents: MviEvent<CounterState>

object ShowNumberIsDivisibleByFiveToast : CounterEvents()