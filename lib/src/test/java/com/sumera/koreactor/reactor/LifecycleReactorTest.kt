package com.sumera.koreactor.reactor

import com.sumera.koreactor.reactor.data.AttachState
import com.sumera.koreactor.reactor.data.CreateState
import com.sumera.koreactor.reactor.data.DestroyState
import com.sumera.koreactor.reactor.data.DetachState
import com.sumera.koreactor.reactor.data.LifecycleState
import com.sumera.koreactor.reactor.data.PauseState
import com.sumera.koreactor.reactor.data.ResumeState
import com.sumera.koreactor.reactor.data.StartState
import com.sumera.koreactor.reactor.data.StopState
import com.sumera.koreactor.testutils.RxTestRule
import com.sumera.koreactor.testutils.TestMviBindableDelegate
import com.sumera.koreactor.testutils.TestMviReactor
import com.sumera.koreactor.testutils.TestObserverWithOrder
import com.sumera.koreactor.testutils.TestState
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LifecycleReactorTest {

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
        lifecycleObserver.assertNextValuesCount()

        reactor.setBindableView(view)

        lifecycleObserver.assertNextValuesCount()

        reactor.onCreate(true)

        lifecycleObserver.assertNextValue(AttachState)
        lifecycleObserver.assertNextValue(CreateState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStart()

        lifecycleObserver.assertNextValue(StartState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onResume()

        lifecycleObserver.assertNextValue(ResumeState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onPause()

        lifecycleObserver.assertNextValue(PauseState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStop()

        lifecycleObserver.assertNextValue(StopState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onDestroy(true)

        lifecycleObserver.assertNextValue(DestroyState)
        lifecycleObserver.assertNextValue(DetachState)
        lifecycleObserver.assertNextValuesCount()
    }

    @Test
    fun reactor_withPausedAndResumedFlow_callsExpectedMethods() {
        lifecycleObserver.assertNextValuesCount()

        reactor.setBindableView(view)

        lifecycleObserver.assertNextValuesCount()

        reactor.onCreate(true)

        lifecycleObserver.assertNextValue(AttachState)
        lifecycleObserver.assertNextValue(CreateState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStart()

        lifecycleObserver.assertNextValue(StartState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onResume()

        lifecycleObserver.assertNextValue(ResumeState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onPause()

        lifecycleObserver.assertNextValue(PauseState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onResume()

        lifecycleObserver.assertNextValue(ResumeState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onPause()

        lifecycleObserver.assertNextValue(PauseState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStop()

        lifecycleObserver.assertNextValue(StopState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onDestroy(true)

        lifecycleObserver.assertNextValue(DestroyState)
        lifecycleObserver.assertNextValue(DetachState)
        lifecycleObserver.assertNextValuesCount()
    }

    @Test
    fun reactor_withStoppedAndStartedFlow_callsExpectedMethods() {
        lifecycleObserver.assertNextValuesCount()

        reactor.setBindableView(view)

        lifecycleObserver.assertNextValuesCount()

        reactor.onCreate(true)

        lifecycleObserver.assertNextValue(AttachState)
        lifecycleObserver.assertNextValue(CreateState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStart()

        lifecycleObserver.assertNextValue(StartState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onResume()

        lifecycleObserver.assertNextValue(ResumeState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onPause()

        lifecycleObserver.assertNextValue(PauseState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStop()

        lifecycleObserver.assertNextValue(StopState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStart()

        lifecycleObserver.assertNextValue(StartState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onResume()

        lifecycleObserver.assertNextValue(ResumeState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onPause()

        lifecycleObserver.assertNextValue(PauseState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStop()

        lifecycleObserver.assertNextValue(StopState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onDestroy(true)

        lifecycleObserver.assertNextValue(DestroyState)
        lifecycleObserver.assertNextValue(DetachState)
        lifecycleObserver.assertNextValuesCount()
    }

    @Test
    fun reactor_withDestroyedAndCreatedFlow_callsExpectedMethods() {
        lifecycleObserver.assertNextValuesCount()

        reactor.setBindableView(view)

        lifecycleObserver.assertNextValuesCount()

        reactor.onCreate(true)

        lifecycleObserver.assertNextValue(AttachState)
        lifecycleObserver.assertNextValue(CreateState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStart()

        lifecycleObserver.assertNextValue(StartState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onResume()

        lifecycleObserver.assertNextValue(ResumeState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onPause()

        lifecycleObserver.assertNextValue(PauseState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStop()

        lifecycleObserver.assertNextValue(StopState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onDestroy(false)

        lifecycleObserver.assertNextValue(DestroyState)
        lifecycleObserver.assertNextValuesCount()

        reactor.setBindableView(view)

        lifecycleObserver.assertNextValuesCount()

        reactor.onCreate(false)

        lifecycleObserver.assertNextValue(CreateState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStart()

        lifecycleObserver.assertNextValue(StartState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onResume()

        lifecycleObserver.assertNextValue(ResumeState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onPause()

        lifecycleObserver.assertNextValue(PauseState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onStop()

        lifecycleObserver.assertNextValue(StopState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onDestroy(true)

        lifecycleObserver.assertNextValue(DestroyState)
        lifecycleObserver.assertNextValue(DetachState)
        lifecycleObserver.assertNextValuesCount()
    }

    @Test
    fun reactor_withFinishedDuringOnCreateFlow_callsExpectedMethods() {
        lifecycleObserver.assertNextValuesCount()

        reactor.setBindableView(view)

        lifecycleObserver.assertNextValuesCount()

        reactor.onCreate(true)

        lifecycleObserver.assertNextValue(AttachState)
        lifecycleObserver.assertNextValue(CreateState)
        lifecycleObserver.assertNextValuesCount()

        reactor.onDestroy(true)

        lifecycleObserver.assertNextValue(DestroyState)
        lifecycleObserver.assertNextValue(DetachState)
        lifecycleObserver.assertNextValuesCount()
    }
}
