package com.sumera.koreactor.behaviour

import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import java.util.*

class Messages<in DATA, STATE: MviState>(
        private var messages: Collection<(DATA) -> MviReactorMessage<STATE>>
) {
    fun applyData(data: DATA): MviReactorMessage<STATE> {
        return MessagesCollection(messages.map { it(data) })
    }
}

fun <DATA, STATE: MviState> messages(message: (DATA) -> MviReactorMessage<STATE>): Messages<DATA, STATE> {
    return Messages(Arrays.asList(message))
}

fun <DATA, STATE: MviState> messages(vararg messages: (DATA) -> MviReactorMessage<STATE>): Messages<DATA, STATE> {
    return Messages(Arrays.asList(*messages))
}
