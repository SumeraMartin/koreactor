package com.sumera.koreactorlib.internal.data

import com.sumera.koreactorlib.reactor.data.MviEvent
import com.sumera.koreactorlib.reactor.data.MviState
import com.sumera.koreactorlib.reactor.lifecycle.LifecycleState

data class EventWithLifecycle<STATE : MviState> (
        val event: MviEvent<STATE>,
        val lifecycleState: LifecycleState
)