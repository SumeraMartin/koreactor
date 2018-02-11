package com.sumera.koreactor.behaviour

import com.sumera.koreactor.internal.data.EventOrReducer
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState

data class MessagesCollection<STATE: MviState>(
        private val values: Collection<MviReactorMessage<STATE>>
) : MviReactorMessage<STATE> {

    override fun messages(): Collection<EventOrReducer<STATE>> {
        return values.flatMap { it.messages() }
    }
}
