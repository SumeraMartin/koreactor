package com.sumera.koreactor.testutils

import com.sumera.koreactor.behaviour.MessagesCollection
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import java.util.*

open class BaseBehaviourTest {

    @Rule @JvmField val reactorTest	= RxTestRule()

    class TestState : MviState

    data class Input(val id: String) : MviAction<TestState>

    data class Output(val id: String) : MviEvent<TestState>()

    data class OutputList(val ids: List<String>, val source: String = "") : MviEvent<TestState>()

    val scheduler: TestScheduler
        get() = reactorTest.scheduler

    val testError = RuntimeException("TestError")

    lateinit var testObserver: TestObserver<MviReactorMessage<TestState>>

    @Before
    fun baseBefore() {
        testObserver = TestObserver()
    }

    fun testMessage(vararg messages: MviReactorMessage<TestState>): MviReactorMessage<TestState> {
        return MessagesCollection(Arrays.asList(*messages))
    }
}