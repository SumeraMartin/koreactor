package com.sumera.koreactor.lib.reactor.data

interface MviReactorMessage<STATE : MviState> {

    fun getMessages(): Collection<EventOrReducer<STATE>>
}
