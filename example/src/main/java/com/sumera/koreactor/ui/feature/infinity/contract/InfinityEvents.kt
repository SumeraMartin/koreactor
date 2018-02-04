package com.sumera.koreactor.ui.feature.infinity.contract

import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.ui.feature.todo.contract.InfinityState

sealed class InfinityEvents : MviEvent<InfinityState>

object NavigateToDetailEvent : InfinityEvents()
