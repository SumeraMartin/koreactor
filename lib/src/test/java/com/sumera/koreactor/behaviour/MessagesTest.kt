package com.sumera.koreactor.behaviour

import com.sumera.koreactor.reactor.data.DispatchedEveryTime
import com.sumera.koreactor.testutils.TestEvent
import com.sumera.koreactor.testutils.TestReducer
import com.sumera.koreactor.testutils.TestState
import org.junit.Assert.assertTrue
import org.junit.Test

class MessagesTest {

    @Test
    fun messages_withAppliedData_returnCorectData() {
        val messages = messages<String, TestState>(
                { TestReducer(it) },
                { TestEvent(id = it, behaviour = DispatchedEveryTime)}
        )

        val appliedMessaged = messages.applyData("TEST")

        assertTrue(appliedMessaged.messages().contains(TestReducer("TEST")))
        assertTrue(appliedMessaged.messages().contains(TestEvent(id = "TEST", behaviour = DispatchedEveryTime)))
    }
}