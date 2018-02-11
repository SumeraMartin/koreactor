package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.testutils.BaseBehaviourTest
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class TimerBehaviourTest : BaseBehaviourTest() {

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
    fun triggeredOnce_shouldStartEmitting() {
        val behaviour = TimerBehaviour<TestState>(
                initialTrigger = triggers(initialSubject),
                duration = 1,
                timeUnit = TimeUnit.SECONDS,
                tickMessage = messages({ Output(id = it.toString()) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext(Input("Test"))

        testObserver.assertValueCount(0)

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0, testMessage(Output(id = "0")))

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1, testMessage(Output(id = "1")))
    }

    @Test
    fun trigerredMultipleTimesWithoutMessage_shouldStartEmittingFromLastTrigger() {
        val behaviour = TimerBehaviour<TestState>(
                initialTrigger = triggers(initialSubject),
                duration = 2,
                timeUnit = TimeUnit.SECONDS,
                tickMessage = messages({ Output(id = it.toString()) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext(Input("Test1"))

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        initialSubject.onNext(Input("Test2"))

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        testObserver.assertValueCount(0)

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0, testMessage(Output(id = "0")))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1, testMessage(Output(id = "1")))
    }

    @Test
    fun trigerredMultipleTimesWithMessage_shouldEmitOneFromFirstAndContinueWithSecond() {
        val behaviour = TimerBehaviour<TestState>(
                initialTrigger = triggers(initialSubject),
                duration = 2,
                timeUnit = TimeUnit.SECONDS,
                tickMessage = messages({ Output(id = it.toString()) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext(Input("Test1"))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0, testMessage(Output(id = "0")))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1, testMessage(Output(id = "1")))

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        initialSubject.onNext(Input("Test2"))

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        testObserver.assertValueCount(3)
        testObserver.assertValueAt(2, testMessage(Output(id = "0")))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(4)
        testObserver.assertValueAt(3, testMessage(Output(id = "1")))
    }

    @Test
    fun triggeredAndCancelled_shouldStopEmitting() {
        val behaviour = TimerBehaviour<TestState>(
                initialTrigger = triggers(initialSubject),
                cancelTrigger = triggers(cancelSubject),
                duration = 2,
                timeUnit = TimeUnit.SECONDS,
                tickMessage = messages({ Output(id = it.toString()) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext(Input("Test1"))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0, testMessage(Output(id = "0")))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1, testMessage(Output(id = "1")))

        cancelSubject.onNext(Input("Test2"))

        scheduler.advanceTimeBy(10, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)
    }

    @Test
    fun triggeredCancelledAndTrigerredAgain_shouldStopAndContinueEmitting() {
        val behaviour = TimerBehaviour<TestState>(
                initialTrigger = triggers(initialSubject),
                cancelTrigger = triggers(cancelSubject),
                duration = 2,
                timeUnit = TimeUnit.SECONDS,
                tickMessage = messages({ Output(id = it.toString()) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext(Input("Test1"))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(1)
        testObserver.assertValueAt(0, testMessage(Output(id = "0")))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)
        testObserver.assertValueAt(1, testMessage(Output(id = "1")))

        cancelSubject.onNext(Input("Test2"))

        scheduler.advanceTimeBy(11, TimeUnit.SECONDS)

        testObserver.assertValueCount(2)

        initialSubject.onNext(Input("Test1"))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(3)
        testObserver.assertValueAt(2, testMessage(Output(id = "0")))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

        testObserver.assertValueCount(4)
        testObserver.assertValueAt(3, testMessage(Output(id = "1")))
    }
}