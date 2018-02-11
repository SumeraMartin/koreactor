package com.sumera.koreactor.behaviour

import com.sumera.koreactor.testutils.RxTestRule
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WorkerTest {

    @Rule @JvmField val reactorTest	= RxTestRule()

    val scheduler: TestScheduler
        get() = reactorTest.scheduler

    lateinit var testSubject: PublishSubject<String>
    lateinit var testObserver: TestObserver<String>
    lateinit var testObserverUnit: TestObserver<Unit>

    var testError = IllegalStateException()

    @Before
    fun before() {
        testSubject = PublishSubject.create()
        testObserver = TestObserver.create()
        testObserverUnit = TestObserver.create()
    }

    @Test
    fun observableWorker_withOnNext_emitsOnNext() {
        val worker = observable<String, String> { testSubject }
        worker.executeAsObservable("IN").subscribe(testObserver)

        testSubject.onNext("OUT1")
        testSubject.onNext("OUT2")

        testObserver.assertValueAt(0, "OUT1")
        testObserver.assertValueAt(1, "OUT2")
        testObserver.assertValueCount(2)
        testObserver.assertNoErrors()
        testObserver.assertNotComplete()
    }

    @Test
    fun observableWorker_withOnNextAndOnError_emitsOnNextAndOnError() {
        val worker = observable<String, String> { testSubject }
        worker.executeAsObservable("IN").subscribe(testObserver)

        testSubject.onNext("OUT1")
        testSubject.onError(testError)

        testObserver.assertValueAt(0, "OUT1")
        testObserver.assertError(testError)
        testObserver.assertValueCount(1)
        testObserver.assertTerminated()
    }

    @Test
    fun singleWorker_withOnNext_emitsOnNextAndTerminate() {
        val worker = single<String, String> { Single.just("OUT1") }
        worker.executeAsObservable("IN").subscribe(testObserver)

        testObserver.assertValueAt(0, "OUT1")
        testObserver.assertValueCount(1)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun singleWorker_withOnError_emitsOnError() {
        val worker = single<String, String> { Single.error(testError) }
        worker.executeAsObservable("IN").subscribe(testObserver)

        testObserver.assertError(testError)
        testObserver.assertValueCount(0)
        testObserver.assertTerminated()
    }

    @Test
    fun maybeWorker_withOnNext_emitsOnNextAndTerminate() {
        val worker = maybe<String, String> { Maybe.just("OUT1") }
        worker.executeAsObservable("IN").subscribe(testObserver)

        testObserver.assertValueAt(0, "OUT1")
        testObserver.assertValueCount(1)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun maybeWorker_withOnError_emitsOnError() {
        val worker = maybe<String, String> { Maybe.error(testError) }
        worker.executeAsObservable("IN").subscribe(testObserver)

        testObserver.assertError(testError)
        testObserver.assertValueCount(0)
        testObserver.assertTerminated()
    }

    @Test
    fun completableWorker_withOnComplete_emitsUnit() {
        val worker = completable<String> { Completable.complete() }

        worker.executeAsObservable("IN").subscribe(testObserverUnit)

        testObserverUnit.assertValueAt(0, Unit)
        testObserverUnit.assertValueCount(1)
        testObserverUnit.assertNoErrors()
        testObserverUnit.assertComplete()
    }

    @Test
    fun completableWorker_withOnError_emitsError() {
        val worker = completable<String> { Completable.error(testError) }

        worker.executeAsObservable("IN").subscribe(testObserverUnit)

        testObserverUnit.assertError(testError)
        testObserverUnit.assertValueCount(0)
        testObserverUnit.assertTerminated()
    }
}