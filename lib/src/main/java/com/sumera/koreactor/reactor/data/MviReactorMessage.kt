package com.sumera.koreactor.reactor.data

import com.sumera.koreactor.internal.data.EventOrReducer

interface MviReactorMessage<STATE : MviState> {

    fun messages(): Collection<EventOrReducer<STATE>>
}
