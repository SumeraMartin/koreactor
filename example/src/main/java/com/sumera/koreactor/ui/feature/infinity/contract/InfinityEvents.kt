package com.sumera.koreactor.ui.feature.infinity.contract

import com.sumera.koreactorlib.reactor.data.MviEvent

sealed class InfinityEvents : MviEvent<InfinityState>

object NavigateToDetailEvent : InfinityEvents()
