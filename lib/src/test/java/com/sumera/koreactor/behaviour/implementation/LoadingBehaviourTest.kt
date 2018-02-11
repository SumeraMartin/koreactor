package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.single
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.testutils.BaseBehaviourTest
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoadingBehaviourTest : BaseBehaviourTest() {

    lateinit var initialSubject: PublishSubject<String>

    lateinit var firstWorkerSubject: PublishSubject<String>

    lateinit var secondWorkerSubject: PublishSubject<String>

    lateinit var firstSingleWorker: Single<String>

    lateinit var secondSingleWorker: Single<String>

    @Before
    fun before() {
        initialSubject = PublishSubject.create()
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

    @Test
    fun trigerred_withNormalFlow_emitsLoadingAndData() {
        val behaviour = LoadingBehaviour<String, String, TestState>(
                triggers = triggers(initialSubject),
                loadWorker = single { firstSingleWorker },
                cancelPrevious = true,
                loadingMessage = messages({ Output(id = "Load " + it) }),
                errorMessage = messages({ Output(id = "Error " + it.message) }),
                dataMessage = messages({ Output(id = "Data " + it) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext("in")

        testObserver.assertValueAt(0, testMessage(Output(id = "Load in")))
        testObserver.assertValueCount(1)

        firstWorkerSubject.onNext("out")

        testObserver.assertValueAt(1, testMessage(Output(id = "Data out")))
        testObserver.assertValueCount(2)
    }

    @Test
    fun triggered_withErroFlow_emitsLoadingAndError() {
        val behaviour = LoadingBehaviour<String, String, TestState>(
                triggers = triggers(initialSubject),
                loadWorker = single { firstSingleWorker },
                cancelPrevious = true,
                loadingMessage = messages({ Output(id = "Load " + it) }),
                errorMessage = messages({ Output(id = "Error " + it.message) }),
                dataMessage = messages({ Output(id = "Data " + it) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext("in")

        testObserver.assertValueAt(0, testMessage(Output(id = "Load in")))
        testObserver.assertValueCount(1)

        firstWorkerSubject.onError(testError)

        testObserver.assertValueAt(1, testMessage(Output(id = "Error TestError")))
        testObserver.assertValueCount(2)
    }

    @Test
    fun trigerred_withCancelPreviousAndMultipleTriggersBeforeEnd_emitsMessagesFromLastOne() {
        val behaviour = LoadingBehaviour<String, String, TestState>(
                triggers = triggers(initialSubject),
                loadWorker = single {
                    if (it == "in1") {
                        firstSingleWorker
                    } else {
                        secondSingleWorker
                    }
                },
                cancelPrevious = true,
                loadingMessage = messages({ Output(id = "Load " + it) }),
                errorMessage = messages({ Output(id = "Error " + it.message) }),
                dataMessage = messages({ Output(id = "Data " + it) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext("in1")

        testObserver.assertValueAt(0, testMessage(Output(id = "Load in1")))
        testObserver.assertValueCount(1)

        initialSubject.onNext("in2")

        firstWorkerSubject.onNext("out1")

        testObserver.assertValueAt(1, testMessage(Output(id = "Load in2")))
        testObserver.assertValueCount(2)

        secondWorkerSubject.onNext("out2")

        testObserver.assertValueAt(2, testMessage(Output(id = "Data out2")))
        testObserver.assertValueCount(3)
    }

    @Test
    fun trigerred_withCancelPreviousTriggerredMultipleTimesAfterEnd_emitsAllMessages() {
        val behaviour = LoadingBehaviour<String, String, TestState>(
                triggers = triggers(initialSubject),
                loadWorker = single {
                    if (it == "in1") {
                        firstSingleWorker
                    } else {
                        secondSingleWorker
                    }
                },
                cancelPrevious = true,
                loadingMessage = messages({ Output(id = "Load " + it) }),
                errorMessage = messages({ Output(id = "Error " + it.message) }),
                dataMessage = messages({ Output(id = "Data " + it) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext("in1")

        testObserver.assertValueAt(0, testMessage(Output(id = "Load in1")))
        testObserver.assertValueCount(1)

        firstWorkerSubject.onNext("out1")

        testObserver.assertValueAt(1, testMessage(Output(id = "Data out1")))
        testObserver.assertValueCount(2)

        initialSubject.onNext("in2")

        testObserver.assertValueAt(2, testMessage(Output(id = "Load in2")))
        testObserver.assertValueCount(3)

        secondWorkerSubject.onNext("out2")

        testObserver.assertValueAt(3, testMessage(Output(id = "Data out2")))
        testObserver.assertValueCount(4)
    }

    @Test
    fun trigerred_withoutCancelPreviousTriggerredMultipleTimesBeforeEnd_emitsAllDataMessages() {
        val behaviour = LoadingBehaviour<String, String, TestState>(
                triggers = triggers(initialSubject),
                loadWorker = single {
                    if (it == "in1") {
                        firstSingleWorker
                    } else {
                        secondSingleWorker
                    }
                },
                cancelPrevious = false,
                loadingMessage = messages({ Output(id = "Load " + it) }),
                errorMessage = messages({ Output(id = "Error " + it.message) }),
                dataMessage = messages({ Output(id = "Data " + it) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext("in1")

        testObserver.assertValueAt(0, testMessage(Output(id = "Load in1")))
        testObserver.assertValueCount(1)

        initialSubject.onNext("in2")

        testObserver.assertValueAt(1, testMessage(Output(id = "Load in2")))
        testObserver.assertValueCount(2)

        firstWorkerSubject.onNext("out1")

        testObserver.assertValueAt(2, testMessage(Output(id = "Data out1")))
        testObserver.assertValueCount(3)

        secondWorkerSubject.onNext("out2")

        testObserver.assertValueAt(3, testMessage(Output(id = "Data out2")))
        testObserver.assertValueCount(4)
    }

    @Test
    fun trigerred_withoutCancelPreviousTriggerredMultipleTimesWithErrorBeforeEnd_emitsAllDataMessagesAndError() {
        val behaviour = LoadingBehaviour<String, String, TestState>(
                triggers = triggers(initialSubject),
                loadWorker = single {
                    if (it == "in1") {
                        firstSingleWorker
                    } else {
                        secondSingleWorker
                    }
                },
                cancelPrevious = false,
                loadingMessage = messages({ Output(id = "Load " + it) }),
                errorMessage = messages({ Output(id = "Error " + it.message) }),
                dataMessage = messages({ Output(id = "Data " + it) })
        ).createObservable()

        behaviour.subscribe(testObserver)

        initialSubject.onNext("in1")

        testObserver.assertValueAt(0, testMessage(Output(id = "Load in1")))
        testObserver.assertValueCount(1)

        initialSubject.onNext("in2")

        testObserver.assertValueAt(1, testMessage(Output(id = "Load in2")))
        testObserver.assertValueCount(2)

        firstWorkerSubject.onError(testError)

        testObserver.assertValueAt(2, testMessage(Output(id = "Error TestError")))
        testObserver.assertValueCount(3)

        secondWorkerSubject.onNext("out2")

        testObserver.assertValueAt(3, testMessage(Output(id = "Data out2")))
        testObserver.assertValueCount(4)
        testObserver.assertNotTerminated()
    }
}