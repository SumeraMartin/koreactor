package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.testutils.BaseBehaviourTest
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class TemporaryBehaviourTest : BaseBehaviourTest() {

    lateinit var initialSubject: PublishSubject<Input>

    lateinit var cancelSubject: PublishSubject<Input>

    @Before
    fun before() {
        initialSubject = PublishSubject.create()
        cancelSubject = PublishSubject.create()
    }

    @After
    fun after() {
        testObserver.assertNotTerminated()
        testObserver.assertNoErrors()
    }

    @Test
    fun triggered_emitsStartAndEnd() {
        val behaviour = TemporaryBehaviour<Input, TestState>(
                triggers = triggers(initialSubject),
                duration = 2,
                timeUnit = TimeUnit.SECONDS,
                cancelPrevious = true,
                startMessage = messages({ Output(id = "Start " + it.id) }),
                endMessage = messages({ Output(id = "End " + it.id) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext(Input("Test"))

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0, testMessage(Output(id = "Start Test")))

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        testObserver.assertValueCount(1)

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1, testMessage(Output(id = "End Test")))
    }

    @Test
    fun triggered_withCancelPreviousAndMultipleTriggersBeforeEnd_emitsEveryStarsAndSingleEnd() {
        val behaviour = TemporaryBehaviour<Input, TestState>(
                triggers = triggers(initialSubject),
                duration = 2,
                timeUnit = TimeUnit.SECONDS,
                cancelPrevious = true,
                startMessage = messages({ Output(id = "Start " + it.id) }),
                endMessage = messages({ Output(id = "End " + it.id) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext(Input("Test1"))

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0, testMessage(Output(id = "Start Test1")))

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        initialSubject.onNext(Input("Test2"))

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1, testMessage(Output(id = "Start Test2")))

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        testObserver.assertValueCount(3)
        testObserver.assertValueAt(2, testMessage(Output(id = "End Test2")))
    }

    @Test
    fun triggered_withCancelPreviousAndMultipleTriggersAfterEnd_emitsEveryStartEventsAndEveryEnd() {
        val behaviour = TemporaryBehaviour<Input, TestState>(
                triggers = triggers(initialSubject),
                duration = 2,
                timeUnit = TimeUnit.SECONDS,
                cancelPrevious = true,
                startMessage = messages({ Output(id = "Start " + it.id) }),
                endMessage = messages({ Output(id = "End " + it.id) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext(Input("Test1"))

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0, testMessage(Output(id = "Start Test1")))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1, testMessage(Output(id = "End Test1")))

        scheduler.advanceTimeBy(10, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)

        initialSubject.onNext(Input("Test2"))

        testObserver.assertValueCount(3)
        testObserver.assertValueAt(2, testMessage(Output(id = "Start Test2")))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(4)
        testObserver.assertValueAt(3, testMessage(Output(id = "End Test2")))

        scheduler.advanceTimeBy(10, TimeUnit.SECONDS)

        testObserver.assertValueCount(4)
    }
}