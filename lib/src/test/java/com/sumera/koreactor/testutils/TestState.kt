package com.sumera.koreactor.testutils

import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviEventBehaviour
import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactor.reactor.data.MviStateReducer

data class TestState(val testID: String = "") : MviState

data class TestReducer(val id: String) : MviStateReducer<TestState> {
    override fun reduce(oldState: TestState): TestState {
        return oldState.copy(testID = id)
    }
}

data class TestEvent(val id: String, val behaviour: MviEventBehaviour) : MviEvent<TestState>() {
    override val eventBehaviour: MviEventBehaviour
        get() = behaviour
}