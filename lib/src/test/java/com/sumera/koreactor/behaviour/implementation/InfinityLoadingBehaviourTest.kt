package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.single
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.testutils.BaseBehaviourTest
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class InfinityLoadingBehaviourTest : BaseBehaviourTest() {

    lateinit var initialSubject: PublishSubject<String>

    lateinit var loadMoreSubject: PublishSubject<String>

    lateinit var firstWorkerSubject: PublishSubject<List<String>>

    lateinit var secondWorkerSubject: PublishSubject<List<String>>

    lateinit var thirdWorkerSubject: PublishSubject<List<String>>

    fun firstSingleWorker(): Single<List<String>> =
            Single.fromObservable(firstWorkerSubject.take(1))

    fun secondSingleWorker(): Single<List<String>> =
            Single.fromObservable(secondWorkerSubject.take(1))

    fun thirdSingleWorker(): Single<List<String>> =
            Single.fromObservable(thirdWorkerSubject.take(1))

    @Before
    fun before() {
        initialSubject = PublishSubject.create()
        loadMoreSubject = PublishSubject.create()
        firstWorkerSubject = PublishSubject.create()
        secondWorkerSubject = PublishSubject.create()
        thirdWorkerSubject = PublishSubject.create()
    }

    @After
    fun after() {
        testObserver.assertNotTerminated()
        testObserver.assertNoErrors()
    }

    @Test
    fun `correct data are passed to worker`() {
        var inputData: InfinityLoadingBehaviour.LoadData<String>? = null
        val behaviour = InfinityLoadingBehaviour<String, String, TestState>(
                initialTriggers = triggers(initialSubject),
                loadMoreTriggers = triggers(loadMoreSubject),
                loadWorker = single {
                    inputData = it
                    firstSingleWorker()
                },
                limit = 10,
                initialOffset = 5,
                initialDataMessage = messages(),
                loadMoreDataMessage = messages(),
                initialErrorMessage = messages(),
                loadingMoreMessage = messages(),
                loadingMoreErrorMessage = messages(),
                initialLoadingMessage= messages(),
                completeMessage = messages()
        ).createObservable()

        behaviour.subscribe(testObserver)

        // Trigger initial input
        initialSubject.onNext("in1")

        // Assert correct input data
        assertNotNull("Worker was not called", inputData)
        assertEquals(InfinityLoadingBehaviour.LoadData("in1", 10, 5), inputData)
    }

    @Test
    fun `load more without initial trigger is ignored`() {
        val behaviour = createDefaultBehaviour(limit = 2)

        behaviour.subscribe(testObserver)

        // Trigger initial input
        loadMoreSubject.onNext("in1")

        // Assert ignored input
        testObserver.assertValueCount(0)
    }

    @Test
    fun `without load more with no more data should emit load,data,complete`() {
        val behaviour = createDefaultBehaviour()

        behaviour.subscribe(testObserver)

        // Trigger initial input
        initialSubject.onNext("in1")

        // Check load message was emitted
        testObserver.assertValueAt(0, testMessage(Output("Load init in1")))
        testObserver.assertValueCount(1)

        // Emit worker result
        firstWorkerSubject.onNext(listOf("out1_1", "out1_2"))

        // Check data and complete messages were emitted
        testObserver.assertValueAt(1, testMessage(OutputList(listOf("out1_1", "out1_2"), "init")))
        testObserver.assertValueAt(2, testMessage(Output("Complete in1")))
        testObserver.assertValueCount(3)
    }

    @Test
    fun `without load more with error should emit load,error`() {
        val behaviour = createDefaultBehaviour(limit = 2)

        behaviour.subscribe(testObserver)

        // Trigger initial input
        initialSubject.onNext("in1")

        // Check load message was emitted
        testObserver.assertValueAt(0, testMessage(Output("Load init in1")))
        testObserver.assertValueCount(1)

        // Emit worker result
        firstWorkerSubject.onError(testError)

        // Check data and error messages were emitted
        testObserver.assertValueAt(1, testMessage(Output("Error init TestError")))
        testObserver.assertValueCount(2)
    }

    @Test
    fun `without load more with no data should emit load,data,complete`() {
        val behaviour = createDefaultBehaviour(limit = 2)

        behaviour.subscribe(testObserver)

        // Trigger initial input
        initialSubject.onNext("in1")

        // Check load message was emitted
        testObserver.assertValueAt(0, testMessage(Output("Load init in1")))
        testObserver.assertValueCount(1)

        // Emit worker result
        firstWorkerSubject.onNext(listOf())

        // Check data and complete messages were emitted
        testObserver.assertValueAt(1, testMessage(OutputList(listOf(), "init")))
        testObserver.assertValueAt(2, testMessage(Output("Complete in1")))
        testObserver.assertValueCount(3)
    }

    @Test
    fun `with load more with complete data should emit loadInit,data,loadMore,data,complete`() {
        val behaviour = createDefaultBehaviourWithMultipleWorkers(limit = 2)

        behaviour.subscribe(testObserver)

        // Trigger initial input
        initialSubject.onNext("in1")

        // Check load message was emitted
        testObserver.assertValueAt(0, testMessage(Output("Load init in1")))
        testObserver.assertValueCount(1)

        // Emit worker result
        firstWorkerSubject.onNext(listOf("out1_1", "out1_2"))

        // Check data message was emitted
        testObserver.assertValueAt(1, testMessage(OutputList(listOf("out1_1", "out1_2"), "init")))
        testObserver.assertValueCount(2)

        // Trigger load more input
        loadMoreSubject.onNext("in2")

        // Check load message was emitted
        testObserver.assertValueAt(2, testMessage(Output("Load more in2")))
        testObserver.assertValueCount(3)

        // Emit worker result
        secondWorkerSubject.onNext(listOf("out2_1"))

        // Check data and complete messages were emitted
        testObserver.assertValueAt(3, testMessage(OutputList(listOf("out2_1"), "more")))
        testObserver.assertValueAt(4, testMessage(Output("Complete in2")))
        testObserver.assertValueCount(5)
    }

    @Test
    fun `with load more with multiple subsequent more data calls should emit loadInit,data,loadMore,data,loadMore,data,complete`() {
        var lastLoadData: InfinityLoadingBehaviour.LoadData<String>? = null
        val behaviour = createDefaultBehaviourWithMultipleWorkers(limit = 2, inputAction = { lastLoadData = it })

        behaviour.subscribe(testObserver)

        // Trigger initial input
        initialSubject.onNext("in1")

        // Check correct loadData
        assertEquals(InfinityLoadingBehaviour.LoadData("in1", 2, 0), lastLoadData)

        // Check load message was emitted
        testObserver.assertValueAt(0, testMessage(Output("Load init in1")))
        testObserver.assertValueCount(1)

        // Emit worker result
        firstWorkerSubject.onNext(listOf("out1_1", "out1_2"))

        // Check data message was emitted
        testObserver.assertValueAt(1, testMessage(OutputList(listOf("out1_1", "out1_2"), "init")))
        testObserver.assertValueCount(2)

        // Trigger load more input
        loadMoreSubject.onNext("in2")

        // Check correct loadData
        assertEquals(InfinityLoadingBehaviour.LoadData("in2", 2, 2), lastLoadData)

        // Check load message was emitted
        testObserver.assertValueAt(2, testMessage(Output("Load more in2")))
        testObserver.assertValueCount(3)

        // Trigger load more input
        loadMoreSubject.onNext("in3")

        // Emit worker result
        secondWorkerSubject.onNext(listOf("out2_1", "out2_2"))

        // Check correct loadData
        assertEquals(InfinityLoadingBehaviour.LoadData("in3", 2, 4), lastLoadData)

        // Check data and loadMore messages were emitted
        testObserver.assertValueAt(3, testMessage(OutputList(listOf("out2_1", "out2_2"), "more")))
        testObserver.assertValueAt(4, testMessage(Output("Load more in3")))
        testObserver.assertValueCount(5)

        // Emit worker result
        thirdWorkerSubject.onNext(listOf("out3_1"))

        // Check data and complete messages were emitted
        testObserver.assertValueAt(5, testMessage(OutputList(listOf("out3_1"), "more")))
        testObserver.assertValueAt(6, testMessage(Output("Complete in3")))
        testObserver.assertValueCount(7)
    }

    @Test
    fun `with load more with multiple subsequent more data calls with error should emit loadInit,data,loadMore,error,loadMore,data,complete`() {
        var lastLoadData: InfinityLoadingBehaviour.LoadData<String>? = null
        val behaviour = createDefaultBehaviourWithMultipleWorkers(limit = 2, inputAction = { lastLoadData = it })

        behaviour.subscribe(testObserver)

        // Trigger initial input
        initialSubject.onNext("in1")

        // Check correct loadData
        assertEquals(InfinityLoadingBehaviour.LoadData("in1", 2, 0), lastLoadData)

        // Check load message was emitted
        testObserver.assertValueAt(0, testMessage(Output("Load init in1")))
        testObserver.assertValueCount(1)

        // Emit worker result
        firstWorkerSubject.onNext(listOf("out1_1", "out1_2"))

        // Check data message was emitted
        testObserver.assertValueAt(1, testMessage(OutputList(listOf("out1_1", "out1_2"), "init")))
        testObserver.assertValueCount(2)

        // Trigger load more input
        loadMoreSubject.onNext("in2")

        // Check load message was emitted
        testObserver.assertValueAt(2, testMessage(Output("Load more in2")))
        testObserver.assertValueCount(3)

        // Trigger load more input
        loadMoreSubject.onNext("in3")

        // Check correct loadData
        assertEquals(InfinityLoadingBehaviour.LoadData("in2", 2, 2), lastLoadData)

        // Emit worker result
        secondWorkerSubject.onError(testError)

        // Check correct loadData
        assertEquals(InfinityLoadingBehaviour.LoadData("in3", 2, 2), lastLoadData)

        // Check data and loadMore messages were emitted
        testObserver.assertValueAt(3, testMessage(Output("Error more TestError")))
        testObserver.assertValueAt(4, testMessage(Output("Load more in3")))
        testObserver.assertValueCount(5)

        // Emit worker result
        thirdWorkerSubject.onNext(listOf("out3_1"))

        // Check data and complete messages were emitted
        testObserver.assertValueAt(5, testMessage(OutputList(listOf("out3_1"), "more")))
        testObserver.assertValueAt(6, testMessage(Output("Complete in3")))
        testObserver.assertValueCount(7)
    }

    @Test
    fun `reload after load more should emit loadInit,data,loadMore,loadInit,data`() {
        var lastLoadData: InfinityLoadingBehaviour.LoadData<String>? = null
        val behaviour = createDefaultBehaviourWithMultipleWorkers(limit = 2, inputAction = { lastLoadData = it })

        behaviour.subscribe(testObserver)

        // Trigger initial input
        initialSubject.onNext("in1")

        // Check correct loadData
        assertEquals(InfinityLoadingBehaviour.LoadData("in1", 2, 0), lastLoadData)

        // Check load message was emitted
        testObserver.assertValueAt(0, testMessage(Output("Load init in1")))
        testObserver.assertValueCount(1)

        // Emit worker result
        firstWorkerSubject.onNext(listOf("out1_1", "out1_2"))

        // Check data message was emitted
        testObserver.assertValueAt(1, testMessage(OutputList(listOf("out1_1", "out1_2"), "init")))
        testObserver.assertValueCount(2)

        // Trigger load more input
        loadMoreSubject.onNext("in2")

        // Check correct loadData
        assertEquals(InfinityLoadingBehaviour.LoadData("in2", 2, 2), lastLoadData)

        // Check load message was emitted
        testObserver.assertValueAt(2, testMessage(Output("Load more in2")))
        testObserver.assertValueCount(3)

        // Trigger initial input
        initialSubject.onNext("in1")

        // Emit worker result which should be ignored
        secondWorkerSubject.onNext(listOf("out2_1", "out2_2"))

        // Check correct loadData
        assertEquals(InfinityLoadingBehaviour.LoadData("in1", 2, 0), lastLoadData)

        // Check load message was emitted
        testObserver.assertValueAt(3, testMessage(Output("Load init in1")))
        testObserver.assertValueCount(4)

        // Emit worker result
        firstWorkerSubject.onNext(listOf("out1_3", "out1_4"))

        // Check load message was emitted
        testObserver.assertValueAt(4, testMessage(OutputList(listOf("out1_3", "out1_4"), "init")))
        testObserver.assertValueCount(5)
    }

    private fun createDefaultBehaviour(limit: Int = 10, offset: Int = 0): Observable<MviReactorMessage<TestState>> {
        return InfinityLoadingBehaviour<String, String, TestState>(
                initialTriggers = triggers(initialSubject),
                loadMoreTriggers = triggers(loadMoreSubject),
                loadWorker = single { firstSingleWorker() },
                limit = limit,
                initialOffset = offset,
                initialDataMessage = messages { OutputList(ids = it, source = "init") },
                loadMoreDataMessage = messages { OutputList(ids = it, source = "more") },
                initialLoadingMessage = messages { Output(id = "Load init $it") },
                loadingMoreMessage = messages { Output(id = "Load more $it") },
                initialErrorMessage = messages { Output(id = "Error init ${it.message}") },
                loadingMoreErrorMessage = messages { Output(id = "Error more ${it.message}") },
                completeMessage = messages { Output(id = "Complete $it") }
        ).createObservable()
    }

    private fun createDefaultBehaviourWithMultipleWorkers(limit: Int = 10, offset: Int = 0, inputAction: (InfinityLoadingBehaviour.LoadData<String>) -> Unit = {}): Observable<MviReactorMessage<TestState>> {
        return InfinityLoadingBehaviour<String, String, TestState>(
                initialTriggers = triggers(initialSubject),
                loadMoreTriggers = triggers(loadMoreSubject),
                loadWorker = single {
                    inputAction(it)
                    when(it.input) {
                        "in1" -> firstSingleWorker()
                        "in2" -> secondSingleWorker()
                        "in3" -> thirdSingleWorker()
                        else -> throw IllegalStateException("unknown input in test")
                    }
                },
                limit = limit,
                initialOffset = offset,
                initialDataMessage = messages { OutputList(ids = it, source = "init") },
                loadMoreDataMessage = messages { OutputList(ids = it, source = "more") },
                initialLoadingMessage = messages { Output(id = "Load init $it") },
                loadingMoreMessage = messages { Output(id = "Load more $it") },
                initialErrorMessage = messages { Output(id = "Error init ${it.message}") },
                loadingMoreErrorMessage = messages { Output(id = "Error more ${it.message}") },
                completeMessage = messages { Output(id = "Complete $it") }
        ).createObservable()
    }
}
