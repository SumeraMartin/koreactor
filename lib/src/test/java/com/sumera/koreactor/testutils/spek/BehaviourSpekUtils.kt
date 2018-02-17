package com.sumera.koreactor.testutils.spek

import com.sumera.koreactor.behaviour.MessagesCollection
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.testutils.TestObserverWithOrder
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import java.util.*

class BehaviourSpekUtils {

    val rxSpekRule = RxSpekRule()

    val scheduler: TestScheduler
        get() = rxSpekRule.scheduler

    val testError = RuntimeException("TestError")

    lateinit var testObserver: TestObserver<MviReactorMessage<TestState>>

    lateinit var testObserverOrder: TestObserverWithOrder<MviReactorMessage<TestState>>

    fun before() {
        rxSpekRule.before()
        testObserverOrder = TestObserverWithOrder()
    }

    fun after() {
        rxSpekRule.after()
    }
}

fun testDispatch(vararg messages: MviReactorMessage<TestState>): MviReactorMessage<TestState> {
    return MessagesCollection(Arrays.asList(*messages))
}
