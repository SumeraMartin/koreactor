package com.sumera.koreactor.reactor.data

interface MviReactorMessage<STATE : MviState> {

    fun getMessages(): Collection<EventOrReducer<STATE>>
}
