package com.sumera.koreactor.reactor

import com.sumera.koreactor.reactor.data.LifecycleState
import com.sumera.koreactor.testutils.RxTestRule
import com.sumera.koreactor.testutils.TestMviBindableDelegate
import com.sumera.koreactor.testutils.TestMviReactor
import com.sumera.koreactor.testutils.TestObserverWithOrder
import com.sumera.koreactor.testutils.TestState
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ViewDelegateReactorTest {

    @Rule
    @JvmField
    val reactorTest	= RxTestRule()

    lateinit var reactor: TestMviReactor

    lateinit var view: TestMviBindableDelegate<TestState>

    lateinit var lifecycleObserver: TestObserverWithOrder<LifecycleState>

    @Before
    fun before() {
        reactor = TestMviReactor()
        view = TestMviBindableDelegate()
        lifecycleObserver = TestObserverWithOrder(reactor.lifecycleTestObserver)
    }

    @Test
    fun reactor_withNormalFlow_callsExpectedMethods() {
        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(0)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.setBindableView(view)

        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(0)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onCreate(true)

        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onStart()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onResume()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onPause()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onStop()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onDestroy(true)

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(1)
        view.assertUnbindActionsCallsCount(1)
    }

    @Test
    fun reactor_withPausedAndResumedFlow_callsExpectedMethods() {
        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(0)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.setBindableView(view)

        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(0)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onCreate(true)

        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onStart()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onResume()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onPause()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onResume()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onPause()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onStop()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onDestroy(true)

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(1)
        view.assertUnbindActionsCallsCount(1)
    }

    @Test
    fun reactor_withStoppedAndStartedFlow_callsExpectedMethods() {
        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(0)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.setBindableView(view)

        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(0)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onCreate(true)

        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onStart()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onResume()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onPause()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onStop()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onStart()

        view.assertBindToStateCallsCount(2)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onResume()

        view.assertBindToStateCallsCount(2)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onPause()

        view.assertBindToStateCallsCount(2)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onStop()

        view.assertBindToStateCallsCount(2)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(2)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onDestroy(true)

        view.assertBindToStateCallsCount(2)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(2)
        view.assertUnbindFromEventsCallsCount(1)
        view.assertUnbindActionsCallsCount(1)
    }

    @Test
    fun reactor_withDestroyedAndCreatedFlow_callsExpectedMethods() {
        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(0)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.setBindableView(view)

        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(0)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onCreate(true)

        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onStart()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onResume()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onPause()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onStop()

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onDestroy(false)

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(1)
        view.assertUnbindActionsCallsCount(1)

        reactor.setBindableView(view)

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(1)
        view.assertUnbindActionsCallsCount(1)

        reactor.onCreate(false)

        view.assertBindToStateCallsCount(1)
        view.assertBindToEventCallsCount(2)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(1)
        view.assertUnbindActionsCallsCount(1)

        reactor.onStart()

        view.assertBindToStateCallsCount(2)
        view.assertBindToEventCallsCount(2)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(1)
        view.assertUnbindActionsCallsCount(1)

        reactor.onResume()

        view.assertBindToStateCallsCount(2)
        view.assertBindToEventCallsCount(2)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(1)
        view.assertUnbindActionsCallsCount(1)

        reactor.onPause()

        view.assertBindToStateCallsCount(2)
        view.assertBindToEventCallsCount(2)
        view.assertUnbindFromStateCallsCount(1)
        view.assertUnbindFromEventsCallsCount(1)
        view.assertUnbindActionsCallsCount(1)

        reactor.onStop()

        view.assertBindToStateCallsCount(2)
        view.assertBindToEventCallsCount(2)
        view.assertUnbindFromStateCallsCount(2)
        view.assertUnbindFromEventsCallsCount(1)
        view.assertUnbindActionsCallsCount(1)

        reactor.onDestroy(true)

        view.assertBindToStateCallsCount(2)
        view.assertBindToEventCallsCount(2)
        view.assertUnbindFromStateCallsCount(2)
        view.assertUnbindFromEventsCallsCount(2)
        view.assertUnbindActionsCallsCount(2)
    }

    @Test
    fun reactor_withFinishedDuringOnCreateFlow_callsExpectedMethods() {
        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(0)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.setBindableView(view)

        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(0)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onCreate(true)

        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(0)
        view.assertUnbindActionsCallsCount(0)

        reactor.onDestroy(true)

        view.assertBindToStateCallsCount(0)
        view.assertBindToEventCallsCount(1)
        view.assertUnbindFromStateCallsCount(0)
        view.assertUnbindFromEventsCallsCount(1)
        view.assertUnbindActionsCallsCount(1)
    }
}
