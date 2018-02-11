package com.sumera.koreactor.behaviour

import com.sumera.koreactor.testutils.RxTestRule
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TriggersTest {

    @Rule @JvmField val reactorTest	= RxTestRule()

    val scheduler: TestScheduler
        get() = reactorTest.scheduler

    lateinit var testSubject: PublishSubject<String>
    lateinit var testObserver: TestObserver<String>

    var testError = RuntimeException()

    @Before
    fun before() {
        testSubject = PublishSubject.create()
        testObserver = TestObserver.create()
    }

    @Test
    fun emptyTriggers_doNothing() {
        val triggers = triggers<String>()

        triggers.merge().subscribe(testObserver)

        testObserver.assertValueCount(0)
        testObserver.assertNoErrors()
        testObserver.assertNotComplete()
    }

    @Test
    fun singleTriggers_emitsOnNext() {
        val triggers = triggers<String>(Single.just("1"), Single.just("2"))

        triggers.merge().subscribe(testObserver)

        testObserver.assertValueAt(0, "1")
        testObserver.assertValueAt(1, "2")
        testObserver.assertValueCount(2)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun observableTriggers_emitsOnNext() {
        val triggers = triggers<String>(Observable.just("1"), testSubject)

        triggers.merge().subscribe(testObserver)

        testSubject.onNext("2")
        testSubject.onNext("3")

        testObserver.assertValueAt(0, "1")
        testObserver.assertValueAt(1, "2")
        testObserver.assertValueAt(2, "3")
        testObserver.assertValueCount(3)
        testObserver.assertNoErrors()
        testObserver.assertNotComplete()
    }

    @Test
    fun triggers_withError_emitsIllegalStateExceptionError() {
        val triggers = triggers<String>(Observable.error(testError), Observable.just("1"))

        triggers.merge().subscribe(testObserver)

        testObserver.assertValueCount(0)
        testObserver.assertError { it is IllegalStateException }
        testObserver.assertTerminated()
    }
}