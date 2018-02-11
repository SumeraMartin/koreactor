package com.sumera.koreactor.reactor

import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.reactor.data.MviStateReducer
import com.sumera.koreactor.testutils.RxTestRule
import com.sumera.koreactor.testutils.TestMviBehaviour
import com.sumera.koreactor.testutils.TestMviBindableDelegate
import com.sumera.koreactor.testutils.TestState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UnbindReactorTest {

    @Rule @JvmField val reactorTest	= RxTestRule()

    abstract class TestReactor : MviReactor<TestState>() {

        val testSubject = PublishSubject.create<MviStateReducer<TestState>>()

        override fun createInitialState(): TestState {
            return TestState()
        }
    }

    lateinit var view: TestMviBindableDelegate<TestState>

    @Before
    fun before() {
        view = TestMviBindableDelegate()
    }

    @Test
    fun bindTo_afterDetach_shouldUnbindObservable() {
        val reactor = object : TestReactor() {
            override fun bind(actions: Observable<MviAction<TestState>>) {
                testSubject.bindTo {  }
            }
        }
        performAndAssertDestroyWithDetach(reactor)
    }

    @Test
    fun bindTo_afterDestroyWithoutDetach_shouldStayBinded() {
        val reactor = object : TestReactor() {
            override fun bind(actions: Observable<MviAction<TestState>>) {
                testSubject.bindTo {  }
            }
        }
        performAndAssertDestroyWithoutDetach(reactor)
    }

    @Test
    fun bindToViewObservable_afterDetach_shouldUnbindObservable() {
        val reactor = object : TestReactor() {
            override fun bind(actions: Observable<MviAction<TestState>>) {
                testSubject.bindToView()
            }
        }
        performAndAssertDestroyWithDetach(reactor)
    }

    @Test
    fun bindToViewObservable_afterDestroyWithoutDetach_shouldStayBinded() {
        val reactor = object : TestReactor() {
            override fun bind(actions: Observable<MviAction<TestState>>) {
                testSubject.bindToView()
            }
        }
        performAndAssertDestroyWithoutDetach(reactor)
    }

    @Test
    fun bindToViewSingle_afterDetach_shouldUnbindObservable() {
        val reactor = object : TestReactor() {
            override fun bind(actions: Observable<MviAction<TestState>>) {
                testSubject.singleOrError().bindToView()
            }
        }
        performAndAssertDestroyWithDetach(reactor)
    }

    @Test
    fun bindToViewSingle_afterDestroyWithoutDetach_shouldStayBinded() {
        val reactor = object : TestReactor() {
            override fun bind(actions: Observable<MviAction<TestState>>) {
                testSubject.singleOrError().bindToView()
            }
        }
        performAndAssertDestroyWithoutDetach(reactor)
    }

    @Test
    fun bindToViewMaybe_afterDetach_shouldUnbindObservable() {
        val reactor = object : TestReactor() {
            override fun bind(actions: Observable<MviAction<TestState>>) {
                testSubject.singleElement().bindToView()
            }
        }
        performAndAssertDestroyWithDetach(reactor)
    }

    @Test
    fun bindToViewMaybe_afterDestroyWithoutDetach_shouldStayBinded() {
        val reactor = object : TestReactor() {
            override fun bind(actions: Observable<MviAction<TestState>>) {
                testSubject.singleElement().bindToView()
            }
        }
        performAndAssertDestroyWithoutDetach(reactor)
    }

    @Test
    fun bindToViewBehaviour_afterDetach_shouldUnbindObservable() {
        val reactor = object : TestReactor() {
            override fun bind(actions: Observable<MviAction<TestState>>) {
                TestMviBehaviour(testTriggers = triggers(testSubject)).bindToView()
            }
        }
        performAndAssertDestroyWithDetach(reactor)
    }

    @Test
    fun bindToViewBehaviour_afterDestroyWithoutDetach_shouldStayBinded() {
        val reactor = object : TestReactor() {
            override fun bind(actions: Observable<MviAction<TestState>>) {
                TestMviBehaviour(testTriggers = triggers(testSubject)).bindToView()
            }
        }
        performAndAssertDestroyWithoutDetach(reactor)
    }

    private fun performAndAssertDestroyWithDetach(reactor: TestReactor) {
        reactor.setBindableView(view)

        assertFalse(reactor.testSubject.hasObservers())

        reactor.onCreate(true)

        assertTrue(reactor.testSubject.hasObservers())

        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()

        assertTrue(reactor.testSubject.hasObservers())

        reactor.onDestroy(true)

        assertFalse(reactor.testSubject.hasObservers())
    }

    private fun performAndAssertDestroyWithoutDetach(reactor: TestReactor) {
        reactor.setBindableView(view)

        assertFalse(reactor.testSubject.hasObservers())

        reactor.onCreate(true)

        assertTrue(reactor.testSubject.hasObservers())

        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()

        assertTrue(reactor.testSubject.hasObservers())

        reactor.onDestroy(false)

        assertTrue(reactor.testSubject.hasObservers())
    }
}
