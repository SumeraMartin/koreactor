package com.sumera.koreactor.reactor

import com.sumera.koreactor.reactor.data.DispatchedEveryTime
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.RequireStartedStateCached
import com.sumera.koreactor.reactor.data.RequireStartedStateNotCached
import com.sumera.koreactor.testutils.RxTestRule
import com.sumera.koreactor.testutils.TestEvent
import com.sumera.koreactor.testutils.TestMviBindableDelegate
import com.sumera.koreactor.testutils.TestState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventReactorTest {

    @Rule
    @JvmField
    val reactorTest	= RxTestRule()

    class TestReactor : MviReactor<TestState>() {

        val testSubject = PublishSubject.create<MviEvent<TestState>>()

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
    fun everytimeEvent_afterOnCreate_isDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)

        sendAndTrigger(TestEvent("test", DispatchedEveryTime))

        view.assertNextEvent(TestEvent("test", DispatchedEveryTime))
        view.assertEventsCount(1)
    }

    @Test
    fun everytimeEvent_afterOnResume_isDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()

        sendAndTrigger(TestEvent("test", DispatchedEveryTime))

        view.assertNextEvent(TestEvent("test", DispatchedEveryTime))
        view.assertEventsCount(1)
    }

    @Test
    fun everytimeEvent_afterOnPause_isDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()

        sendAndTrigger(TestEvent("test", DispatchedEveryTime))

        view.assertNextEvent(TestEvent("test", DispatchedEveryTime))
        view.assertEventsCount(1)
    }

    @Test
    fun everytimeEvent_afterOnStop_isDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()

        sendAndTrigger(TestEvent("test", DispatchedEveryTime))

        view.assertNextEvent(TestEvent("test", DispatchedEveryTime))
        view.assertEventsCount(1)
    }

    @Test
    fun everytimeEvent_afterOnDestroy_isNotDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(false)

        sendAndTrigger(TestEvent("test", DispatchedEveryTime))

        view.assertEventsCount(0)
    }

    @Test
    fun everytimeEvent_afterOnDestroyWithOncreate_isDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(false)

        sendAndTrigger(TestEvent("test", DispatchedEveryTime))

        reactor.setBindableView(view)
        reactor.onCreate(false)

        view.assertNextEvent(TestEvent("test", DispatchedEveryTime))
        view.assertEventsCount(1)
    }

    @Test
    fun requireStartedNotCached_afterOnCreate_isThrownAway() {
        reactor.setBindableView(view)
        reactor.onCreate(true)

        sendAndTrigger(TestEvent("test", RequireStartedStateNotCached))

        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(true)

        view.assertEventsCount(0)
    }

    @Test
    fun requireStartedNotCached_afterOnStart_isDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        sendAndTrigger(TestEvent("test", RequireStartedStateNotCached))

        view.assertNextEvent(TestEvent("test", RequireStartedStateNotCached))
        view.assertEventsCount(1)

        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(true)

        view.assertEventsCount(1)
    }

    @Test
    fun requireStartedNotCached_afterOnResume_isDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()

        sendAndTrigger(TestEvent("test", RequireStartedStateNotCached))

        view.assertNextEvent(TestEvent("test", RequireStartedStateNotCached))
        view.assertEventsCount(1)

        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(true)

        view.assertEventsCount(1)
    }

    @Test
    fun requireStartedNotCached_afterOnPause_isDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()

        sendAndTrigger(TestEvent("test", RequireStartedStateNotCached))

        view.assertNextEvent(TestEvent("test", RequireStartedStateNotCached))
        view.assertEventsCount(1)

        reactor.onStop()
        reactor.onDestroy(true)

        view.assertEventsCount(1)
    }

    @Test
    fun requireStartedNotCached_afterOnStop_isThrownAway() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()

        sendAndTrigger(TestEvent("test", RequireStartedStateNotCached))

        reactor.onDestroy(false)
        reactor.setBindableView(view)
        reactor.onCreate(false)
        reactor.onStart()
        reactor.onResume()

        view.assertEventsCount(0)
    }

    @Test
    fun requireStartedNotCached_afterOnDestroy_isThrownAway() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(false)

        sendAndTrigger(TestEvent("test", RequireStartedStateNotCached))

        reactor.setBindableView(view)
        reactor.onCreate(false)
        reactor.onStart()
        reactor.onResume()

        view.assertEventsCount(0)
    }

    @Test
    fun requireStartedCached_afterOnCreateWithOnStart_isCachedAndDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)

        sendAndTrigger(TestEvent("test", RequireStartedStateCached))

        view.assertEventsCount(0)

        reactor.onStart()

        view.assertNextEvent(TestEvent("test", RequireStartedStateCached))
        view.assertEventsCount(1)

        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(true)

        view.assertEventsCount(1)
    }

    @Test
    fun requireStartedCached_afterOnStart_isDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        sendAndTrigger(TestEvent("test", RequireStartedStateCached))

        view.assertNextEvent(TestEvent("test", RequireStartedStateCached))
        view.assertEventsCount(1)

        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(true)

        view.assertEventsCount(1)
    }

    @Test
    fun requireStartedCached_afterOnResume_isDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()

        sendAndTrigger(TestEvent("test", RequireStartedStateCached))

        view.assertNextEvent(TestEvent("test", RequireStartedStateCached))
        view.assertEventsCount(1)

        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(true)

        view.assertEventsCount(1)
    }

    @Test
    fun requireStartedCached_afterOnPause_isDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()

        sendAndTrigger(TestEvent("test", RequireStartedStateCached))

        view.assertNextEvent(TestEvent("test", RequireStartedStateCached))
        view.assertEventsCount(1)

        reactor.onStop()
        reactor.onDestroy(true)

        view.assertEventsCount(1)
    }

    @Test
    fun requireStartedCached_afterOnStopWithRecreate_isCachedAndDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()

        sendAndTrigger(TestEvent("test", RequireStartedStateCached))

        view.assertEventsCount(0)

        reactor.onDestroy(false)
        reactor.setBindableView(view)
        reactor.onCreate(false)
        reactor.onStart()

        view.assertNextEvent(TestEvent("test", RequireStartedStateCached))
        view.assertEventsCount(1)
    }

    @Test
    fun requireStartedCached_afterOnDestroyWithRecreate_isCachedAndDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(false)

        sendAndTrigger(TestEvent("test", RequireStartedStateCached))

        view.assertEventsCount(0)

        reactor.setBindableView(view)
        reactor.onCreate(false)
        reactor.onStart()

        view.assertNextEvent(TestEvent("test", RequireStartedStateCached))
        view.assertEventsCount(1)
    }

    @Test
    fun requireStartedCachedMultiple_afterOnCreateWithOnStart_isCachedAndDispatched() {
        reactor.setBindableView(view)
        reactor.onCreate(true)

        sendAndTrigger(TestEvent("test1", RequireStartedStateCached))
        sendAndTrigger(TestEvent("test2", RequireStartedStateCached))

        reactor.onStart()

        sendAndTrigger(TestEvent("test3", RequireStartedStateCached))

        view.assertNextEvent(TestEvent("test1", RequireStartedStateCached))
        view.assertNextEvent(TestEvent("test2", RequireStartedStateCached))
        view.assertNextEvent(TestEvent("test3", RequireStartedStateCached))
        view.assertEventsCount(3)

        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(true)
    }

    @Test
    fun differentBehaviours_afterOnCreateWithOnStart_areDispatchedByTheirBehaviours() {
        reactor.setBindableView(view)
        reactor.onCreate(true)

        sendAndTrigger(TestEvent("test1", DispatchedEveryTime))
        sendAndTrigger(TestEvent("test2", RequireStartedStateCached))
        sendAndTrigger(TestEvent("test3", RequireStartedStateNotCached))

        view.assertNextEvent(TestEvent("test1", DispatchedEveryTime))
        view.assertEventsCount(1)

        reactor.onStart()

        view.assertNextEvent(TestEvent("test2", RequireStartedStateCached))
        view.assertEventsCount(2)

        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(true)
    }

    @Test
    fun differentBehaviours_afterOnStart_areDispatchedAll() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()

        sendAndTrigger(TestEvent("test1", DispatchedEveryTime))
        sendAndTrigger(TestEvent("test2", RequireStartedStateCached))
        sendAndTrigger(TestEvent("test3", RequireStartedStateNotCached))

        view.assertNextEvent(TestEvent("test1", DispatchedEveryTime))
        view.assertNextEvent(TestEvent("test2", RequireStartedStateCached))
        view.assertNextEvent(TestEvent("test3", RequireStartedStateNotCached))
        view.assertEventsCount(3)

        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(true)
    }

    @Test
    fun differentBehaviours_afterOnStopWithRecreate_areDispatchedByTheirBehaviours() {
        reactor.setBindableView(view)
        reactor.onCreate(true)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()

        sendAndTrigger(TestEvent("test1", DispatchedEveryTime))
        sendAndTrigger(TestEvent("test2", RequireStartedStateCached))
        sendAndTrigger(TestEvent("test3", RequireStartedStateNotCached))
        sendAndTrigger(TestEvent("test4", RequireStartedStateCached))

        view.assertNextEvent(TestEvent("test1", DispatchedEveryTime))
        view.assertEventsCount(1)

        reactor.onDestroy(false)
        reactor.setBindableView(view)
        reactor.onCreate(false)
        reactor.onStart()

        view.assertNextEvent(TestEvent("test2", RequireStartedStateCached))
        view.assertNextEvent(TestEvent("test4", RequireStartedStateCached))
        view.assertEventsCount(3)
    }

    private fun sendAndTrigger(reducer: MviEvent<TestState>) {
        reactor.testSubject.onNext(reducer)
        reactorTest.scheduler.triggerActions()
    }
}
