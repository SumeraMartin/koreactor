package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.dispatch
import com.sumera.koreactor.behaviour.single
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.testutils.BaseBehaviourTest
import com.sumera.koreactor.testutils.it
import com.sumera.koreactor.testutils.on
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test

class SwipeRefreshLoadingBehaviourTest : BaseBehaviourTest() {

    lateinit var initialTrigger: PublishSubject<String>
    lateinit var swipeRefreshTrigger: PublishSubject<String>
    lateinit var firstWorkerSubject: PublishSubject<String>
    lateinit var secondWorkerSubject: PublishSubject<String>
    lateinit var firstSingleWorker: Single<String>
    lateinit var secondSingleWorker: Single<String>

    @Before
    fun before() {
        initialTrigger = PublishSubject.create()
        swipeRefreshTrigger = PublishSubject.create()
        firstWorkerSubject = PublishSubject.create()
        secondWorkerSubject = PublishSubject.create()
        firstSingleWorker = Single.fromObservable(firstWorkerSubject.take(1))
        secondSingleWorker = Single.fromObservable(secondWorkerSubject.take(1))
    }

    @After
    fun after() {
        testObserver.assertNotTerminated()
        testObserver.assertNoErrors()
    }

    fun prepareBehaviour(cancelPrevious: Boolean = true) {
        SwipeRefreshLoadingBehaviour<String, String, TestState>(
                initialTriggers = triggers(initialTrigger),
                swipeRefreshTriggers = triggers(swipeRefreshTrigger),
                loadWorker = single {
                    when(it.data) {
                        "in1" -> firstSingleWorker
                        "in2" -> secondSingleWorker
                        else -> throw IllegalStateException("Unknown worker")
                    }
                },
                cancelPrevious = true,
                onInitialLoading = dispatch { Output("Init load ${it.data}") },
                onSwipeRefreshLoading = dispatch { Output("Swipe load ${it.data}") },
                onInitialError = dispatch { Output("Init error ${it.data}") },
                onSwipeRefreshError = dispatch { Output("Swipe error ${it.data}") },
                onInitialData = dispatch { Output("Init data ${it.output}") },
                onSwipeRefreshData = dispatch { Output("Swipe data ${it.output}") }
        ).createObservable().subscribe(testObserver)
    }

    @Test
    fun `with initial trigger action dispatch load,data`() {
        prepareBehaviour()

        on("first initial trigger action") {
            initialTrigger.onNext("in1")

            it("dispatch initial loading message") {
                testObserverOrder.assertNextValue(testMessage(Output("Init load in1")))
                testObserverOrder.assertNextValuesCount()
            }
        }

        on("first worker result") {
            firstWorkerSubject.onNext("in1")

            it("dispatch initial data message") {
                testObserverOrder.assertNextValue(testMessage(Output("Init data in1")))
                testObserverOrder.assertNextValuesCount()
            }
        }
    }

    @Test
    fun `with initial trigger action and error dispatch load,error`() {
        prepareBehaviour()

        on("first initial trigger action") {
            initialTrigger.onNext("in1")

            it("dispatch initial loading message") {
                testObserverOrder.assertNextValue(testMessage(Output("Init load in1")))
                testObserverOrder.assertNextValuesCount()
            }
        }

        on("first worker result") {
            firstWorkerSubject.onNext("in1")

            it("dispatch initial data message") {
                testObserverOrder.assertNextValue(testMessage(Output("Init data in1")))
                testObserverOrder.assertNextValuesCount()
            }
        }
    }

}