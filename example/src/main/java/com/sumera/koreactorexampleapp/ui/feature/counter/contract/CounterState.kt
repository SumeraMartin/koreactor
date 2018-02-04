package com.sumera.koreactorexampleapp.ui.feature.counter.contract

import com.sumera.koreactor.reactor.data.MviState

data class CounterState(
		val counter: Int
): MviState
