package com.sumera.koreactor.internal.data

import com.sumera.koreactor.reactor.data.LifecycleState
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviState

data class EventWithLifecycle<STATE : MviState> (
        val event: MviEvent<STATE>,
        val lifecycleState: LifecycleState
)