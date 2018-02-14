package com.sumera.koreactor.behaviour

import com.sumera.koreactor.behaviour.data.ErrorWithData
import com.sumera.koreactor.behaviour.data.InputData
import com.sumera.koreactor.behaviour.data.OutputData
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import java.util.*

typealias InputMessages<DATA, STATE> = Messages<InputData<DATA>, STATE>
typealias OutputMessages<INPUT, OUTPUT, STATE> = Messages<OutputData<INPUT, OUTPUT>, STATE>
typealias ErrorMessages<DATA, STATE> = Messages<ErrorWithData<DATA>, STATE>

open class Messages<in DATA, STATE: MviState>(
        private var messages: Collection<(DATA) -> MviReactorMessage<STATE>>
) {
    fun applyData(data: DATA): MviReactorMessage<STATE> {
        return MessagesCollection(messages.map { it(data) })
    }
}

fun <DATA, STATE: MviState> dispatch(message: (DATA) -> MviReactorMessage<STATE>): Messages<DATA, STATE> {
    return Messages(Arrays.asList(message))
}

fun <DATA, STATE: MviState> dispatch(vararg messages: (DATA) -> MviReactorMessage<STATE>): Messages<DATA, STATE> {
    return Messages(Arrays.asList(*messages))
}
