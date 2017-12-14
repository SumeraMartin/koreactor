package com.sumera.koreactor.ui.feature.counter.contract

import com.sumera.koreactor.lib.reactor.data.event.MviEvent

sealed class CounterEvents: MviEvent<CounterState>

object ShowNumberIsDivisibleByFiveToast : CounterEvents()