package com.sumera.koreactor.ui.feature.counter.contract

import com.sumera.koreactorlib.reactor.data.MviState

data class CounterState(
		val counter: Int
): MviState
