package com.sumera.koreactor.lib.internal.data

import com.sumera.koreactor.lib.reactor.data.event.MviEvent
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.lifecycle.LifecycleEvent

data class EventWithLifecycle<STATE : MviState> (
		val event: MviEvent<STATE>,
		val lifecycleEvent: LifecycleEvent
)