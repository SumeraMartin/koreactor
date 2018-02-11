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

class AbstractMethodsReactorTest {

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
        reactor.assertBindCallsCount(0)
        reactor.assertCreateInitialStateCallsCount(0)

        reactor.setBindableView(view)

        reactor.assertBindCallsCount(0)
        reactor.assertCreateInitialStateCallsCount(0)

        reactor.onCreate(true)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStart()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onResume()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onPause()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStop()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onDestroy(true)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)
    }

    @Test
    fun reactor_withPausedAndResumedFlow_callsExpectedMethods() {
        reactor.assertBindCallsCount(0)
        reactor.assertCreateInitialStateCallsCount(0)

        reactor.setBindableView(view)

        reactor.assertBindCallsCount(0)
        reactor.assertCreateInitialStateCallsCount(0)

        reactor.onCreate(true)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStart()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onResume()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onPause()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onResume()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onPause()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStop()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onDestroy(true)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)
    }

    @Test
    fun reactor_withStoppedAndStartedFlow_callsExpectedMethods() {
        reactor.assertBindCallsCount(0)
        reactor.assertCreateInitialStateCallsCount(0)

        reactor.setBindableView(view)

        reactor.assertBindCallsCount(0)
        reactor.assertCreateInitialStateCallsCount(0)

        reactor.onCreate(true)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStart()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onResume()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onPause()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStop()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStart()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onResume()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onPause()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStop()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onDestroy(true)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)
    }

    @Test
    fun reactor_withDestroyedAndCreatedFlow_callsExpectedMethods() {
        reactor.assertBindCallsCount(0)
        reactor.assertCreateInitialStateCallsCount(0)

        reactor.setBindableView(view)

        reactor.assertBindCallsCount(0)
        reactor.assertCreateInitialStateCallsCount(0)

        reactor.onCreate(true)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStart()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onResume()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onPause()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStop()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onDestroy(false)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.setBindableView(view)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onCreate(false)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStart()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onResume()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onPause()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onStop()

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onDestroy(true)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)
    }

    @Test
    fun reactor_withFinishedDuringOnCreateFlow_callsExpectedMethods() {
        reactor.assertBindCallsCount(0)
        reactor.assertCreateInitialStateCallsCount(0)

        reactor.setBindableView(view)

        reactor.assertBindCallsCount(0)
        reactor.assertCreateInitialStateCallsCount(0)

        reactor.onCreate(true)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)

        reactor.onDestroy(true)

        reactor.assertBindCallsCount(1)
        reactor.assertCreateInitialStateCallsCount(1)
    }
}
