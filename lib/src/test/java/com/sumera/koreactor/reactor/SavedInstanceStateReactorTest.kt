package com.sumera.koreactor.reactor

import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.testutils.RxTestRule
import com.sumera.koreactor.testutils.TestMviBindableDelegate
import com.sumera.koreactor.testutils.TestState
import com.sumera.koreactor.util.bundle.BundleWrapper
import com.sumera.koreactor.util.bundle.InMemoryBundleWrapper
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SavedInstanceStateReactorTest {

    @Rule @JvmField val reactorTest = RxTestRule()

    companion object {
        private const val INITIAL_VALUE = "x"
        private const val CHANGED_VALUE = "y"
        private const val KEY = "key"
    }

    class TestReactor : MviReactor<TestState>() {

        override fun createInitialState(): TestState {
            return TestState(testID = INITIAL_VALUE)
        }

        override fun onSaveInstanceState(state: TestState, bundleWrapper: BundleWrapper) {
            bundleWrapper.putString(KEY, state.testID)
        }

        override fun onRestoreSaveInstanceState(state: TestState, bundleWrapper: BundleWrapper): TestState {
            val savedValue = bundleWrapper.getString(KEY)!!
            return state.copy(testID = savedValue)
        }

        override fun bind(actions: Observable<MviAction<TestState>>) {
            // Do nothing
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
    fun `reactor should save the state to bundle after the normal flow`() {
        // GIVEN
        val bundle = InMemoryBundleWrapper()

        // WHEN
        reactor.setBindableView(view)
        reactor.onCreate(null)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onSaveInstanceState(bundle)
        reactor.onStop()
        reactor.onDestroy(true)

        // THEN
        assertEquals(INITIAL_VALUE, bundle.getString(KEY))
    }

    @Test
    fun `reactor should not recreate state from bundle after the rotation flow`() {
        // GIVEN
        val bundle = InMemoryBundleWrapper().apply {
            putString(KEY, CHANGED_VALUE)
        }

        // WHEN
        reactor.setBindableView(view)
        reactor.onCreate(null)
        reactor.onStart()
        reactor.onResume()
        reactor.onPause()
        reactor.onStop()
        reactor.onDestroy(false)
        reactor.setBindableView(view)
        reactor.onCreate(bundle)
        reactor.onStart()

        // THEN
        view.assertLastState(TestState(INITIAL_VALUE))
    }

    @Test
    fun `reactor should change the state according to value saved to bundle`() {
        // GIVEN
        val bundle = InMemoryBundleWrapper().apply {
            putString(KEY, CHANGED_VALUE)
        }

        // WHEN
        reactor.setBindableView(view)
        reactor.onCreate(bundle)
        reactor.onStart()

        // THEN
        view.assertNextState(TestState(CHANGED_VALUE))
    }

    @Test
    fun `reactor should emit the only one state when created from bundle`() {
        // GIVEN
        val bundle = InMemoryBundleWrapper().apply {
            putString(KEY, CHANGED_VALUE)
        }

        // WHEN
        reactor.setBindableView(view)
        reactor.onCreate(bundle)
        reactor.onStart()

        // THEN
        view.assertStatesCount(1)
    }
}