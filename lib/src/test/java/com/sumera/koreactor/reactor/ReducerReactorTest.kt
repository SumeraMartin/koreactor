package com.sumera.koreactor.reactor

import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.reactor.data.MviStateReducer
import com.sumera.koreactor.testutils.RxTestRule
import com.sumera.koreactor.testutils.TestMviBindableDelegate
import com.sumera.koreactor.testutils.TestReducer
import com.sumera.koreactor.testutils.TestState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReducerReactorTest {

    @Rule
    @JvmField
    val reactorTest	= RxTestRule()

    class TestReactor : MviReactor<TestState>() {

        val testSubject = PublishSubject.create<MviStateReducer<TestState>>()

        override fun createInitialState(): TestState {
            return TestState()
        }

        override fun bind(actions: Observable<MviAction<TestState>>) {
            testSubject.bindToView()
        }
    }

    lateinit var reactor: TestReactor

    lateinit var view: TestMviBindableDelegate<TestState>

    @Before
    fun before() {
        reactor = TestReactor()
        view = TestMviBindableDelegate()
    }

    @Test
    fun reducer_afterOnCreate_isNotDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)

        sendAndTrigger(TestReducer("test"))

        view.assertEventsCount(0)
    }

    @Test
    fun reducer_afterOnCreate_isDispatchedAfterOnStart() {
        reactor.setBindableView(view)
        reactor.onCreate(true)

        sendAndTrigger(TestReducer("test"))

        reactor.onStart()

        view.assertNextState(TestState(testID = "test"))
        view.assertStatesCount(1)
    }

    @Test
    fun reducer_afterOnStart_isDispatchedToViewDirectly() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        view.assertNextState(TestState(""))
        view.assertStatesCount(1)

        sendAndTrigger(TestReducer("test"))

        view.assertNextState(TestState("test"))
        view.assertStatesCount(2)
        view.assertNoMoreStates()
    }

    @Test
    fun reducer_afterOnResume_isDispatchedToViewDirectly() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        view.assertNextState(TestState(""))
        view.assertStatesCount(1)

        reactor.onResume()

        sendAndTrigger(TestReducer("test"))

        view.assertNextState(TestState("test"))
        view.assertStatesCount(2)
        view.assertNoMoreStates()
    }

    @Test
    fun reducer_afterOnPause_isDispatchedToViewDirectly() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        view.assertNextState(TestState(""))
        view.assertStatesCount(1)

        reactor.onResume()
        reactor.onPause()

        sendAndTrigger(TestReducer("test"))

        view.assertNextState(TestState("test"))
        view.assertStatesCount(2)
        view.assertNoMoreStates()
    }

    @Test
    fun reducer_afterOnStop_isNotDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        view.assertNextState(TestState(""))
        view.assertStatesCount(1)

        reactor.onResume()
        reactor.onPause()
        reactor.onStop()

        view.assertEventsCount(0)

        sendAndTrigger(TestReducer("test"))

        view.assertLastState(TestState(""))
        view.assertStatesCount(1)
        view.assertNoMoreStates()
    }

    @Test
    fun reducer_afterOnStopWithOnStart_isDispacthedAfterOnStart() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        view.assertNextState(TestState(""))
        view.assertStatesCount(1)

        reactor.onResume()
        reactor.onPause()
        reactor.onStop()

        sendAndTrigger(TestReducer("test"))

        reactor.onStart()

        view.assertLastState(TestState("test"))
        view.assertStatesCount(2)
        view.assertNoMoreStates()
    }

    @Test
    fun reducer_afterOnStopWithRecreate_isDispacthedAfterOnStart() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        view.assertNextState(TestState(""))
        view.assertStatesCount(1)

        reactor.onResume()
        reactor.onPause()
        reactor.onStop()

        sendAndTrigger(TestReducer("test"))

        reactor.onDestroy(false)
        reactor.setBindableView(view)
        reactor.onCreate(false)
        reactor.onStart()

        view.assertLastState(TestState("test"))
        view.assertStatesCount(2)
        view.assertNoMoreStates()
    }

    @Test
    fun reducer_afterOnDestroy_isNotDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        view.assertNextState(TestState(""))
        view.assertStatesCount(1)

        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(false)

        view.assertEventsCount(0)

        sendAndTrigger(TestReducer("test"))

        view.assertLastState(TestState(""))
        view.assertStatesCount(1)
        view.assertNoMoreStates()
    }

    @Test
    fun reducer_afterOnDestroyWithRecreate_isNotDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        view.assertNextState(TestState(""))
        view.assertStatesCount(1)

        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(false)

        sendAndTrigger(TestReducer("test"))

        reactor.setBindableView(view)
        reactor.onCreate(false)
        reactor.onStart()

        view.assertLastState(TestState("test"))
        view.assertStatesCount(2)
        view.assertNoMoreStates()
    }

    @Test
    fun reducers_afterOnStart_areDispatchedInOrder() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        view.assertNextState(TestState(""))
        view.assertStatesCount(1)

        sendAndTrigger(TestReducer("test"))

        view.assertLastState(TestState("test"))
        view.assertStatesCount(2)

        sendAndTrigger(TestReducer("test2"))

        view.assertLastState(TestState("test2"))
        view.assertStatesCount(3)
    }

    @Test
    fun reducers_afterOnCreateWithOnStart_isDispatchedOnlyLast() {
        reactor.setBindableView(view)
        reactor.onCreate(true)

        sendAndTrigger(TestReducer("test"))
        sendAndTrigger(TestReducer("test2"))

        reactor.onStart()

        view.assertLastState(TestState("test2"))
        view.assertStatesCount(1)
    }

    private fun sendAndTrigger(reducer: MviStateReducer<TestState>) {
        reactor.testSubject.onNext(reducer)
        reactorTest.scheduler.triggerActions()
    }
}
