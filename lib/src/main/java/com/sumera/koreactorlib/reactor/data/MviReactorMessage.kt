package com.sumera.koreactorlib.reactor.data

interface MviReactorMessage<STATE : MviState> {

    fun getMessages(): Collection<EventOrReducer<STATE>>
}
