package com.sumera.koreactor.lib.behaviour

import com.sumera.koreactor.lib.reactor.data.MviReactorMessage
import com.sumera.koreactor.lib.reactor.data.MviReactorMessageCollection
import com.sumera.koreactor.lib.reactor.data.MviState
import java.util.*

class Messages<in DATA, STATE: MviState>(
        private var messages: Collection<(DATA) -> MviReactorMessage<STATE>>
) {
    fun applyData(data: DATA): MviReactorMessage<STATE> {
        return MviReactorMessageCollection(messages.map { it(data) })
    }
}

fun <DATA, STATE: MviState> messages(vararg messages: (DATA) -> MviReactorMessage<STATE>): Messages<DATA, STATE> {
    return Messages(Arrays.asList(*messages))
}
