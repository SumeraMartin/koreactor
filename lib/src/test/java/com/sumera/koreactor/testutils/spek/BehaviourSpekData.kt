package com.sumera.koreactor.testutils.spek

import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviState

class TestState : MviState

data class Input(val id: String) : MviAction<TestState>

data class TestEvent(val id: String) : MviEvent<TestState>()

data class OutputList(val ids: List<String>, val source: String = "") : MviEvent<TestState>()

class TestError : RuntimeException("TestError")

