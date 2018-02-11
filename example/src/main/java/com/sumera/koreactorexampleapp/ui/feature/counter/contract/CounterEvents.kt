package com.sumera.koreactorexampleapp.ui.feature.counter.contract

import com.sumera.koreactor.reactor.data.MviEvent

sealed class CounterEvents: MviEvent<CounterState>()

object ShowNumberIsDivisibleByFiveToast : CounterEvents()