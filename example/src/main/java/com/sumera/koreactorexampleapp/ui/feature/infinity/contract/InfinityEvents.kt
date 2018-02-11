package com.sumera.koreactorexampleapp.ui.feature.infinity.contract

import com.sumera.koreactor.reactor.data.MviEvent

sealed class InfinityEvents : MviEvent<InfinityState>()

object NavigateToDetailEvent : InfinityEvents()
