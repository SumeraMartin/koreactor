package com.sumera.koreactor.lib.internal.data

import com.sumera.koreactor.lib.reactor.data.MviEvent
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.lifecycle.LifecycleState

data class EventWithLifecycle<STATE : MviState> (
        val event: MviEvent<STATE>,
        val lifecycleState: LifecycleState
)