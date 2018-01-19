package com.sumera.koreactor

import com.sumera.koreactor.lib.ReactorTestRule
import com.sumera.koreactor.lib.annotation.InitialLifecycleState
import com.sumera.koreactor.lib.annotation.RunAfter
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.ui.feature.counter.CounterReactor
import com.sumera.koreactor.ui.feature.counter.contract.CounterState
import com.sumera.koreactor.ui.feature.counter.contract.DecrementAction
import com.sumera.koreactor.ui.feature.counter.contract.IncrementAction
import com.sumera.koreactor.ui.feature.counter.contract.ShowNumberIsDivisibleByFiveToast
import org.junit.Rule
import org.junit.Test

class CounterRectorTest {

	@Rule @JvmField
	val reactorTest	 = object : ReactorTestRule<CounterState>() {
		override fun createNewReactorInstance(): MviReactor<CounterState> {
			return CounterReactor()
		}
	}

	@Test
	@RunAfter(InitialLifecycleState.ON_RESUME)
	fun decrement() {
		reactorTest.runTest {

			assertNextState { CounterState(counter = 0) }

			sendAction { DecrementAction }

			assertNextState { CounterState(counter = -1) }

			sendAction { DecrementAction }

			assertNextState { CounterState(counter = -2) }

			assertStatesCount(3)
			assertNoMoreStates()
		}
	}

	@Test
	@RunAfter(InitialLifecycleState.ON_RESUME)
	fun increment() {
		reactorTest.runTest {

			assertNextState(CounterState(counter = 0))

			sendAction(IncrementAction)

			assertNextState(CounterState(counter = 1))

			sendAction(IncrementAction)

			assertNextState(CounterState(counter = 2))

			assertStatesCount(3)
			assertNoMoreStates()
		}
	}

	@Test
	@RunAfter(InitialLifecycleState.ON_RESUME)
	fun showNumberIsDivisibleByFiveToast_withNumberDivisibleByFive_showToast() {
		reactorTest.runTest {

			repeat(5, { sendAction(IncrementAction) })

			assertNextEvent(ShowNumberIsDivisibleByFiveToast)

			assertEventsCount(1)
			assertNoMoreEvents()
		}
	}

	@Test
	@RunAfter(InitialLifecycleState.ON_RESUME)
	fun showNumberIsDivisibleByFiveToast_withZero_doNothing() {
		reactorTest.runTest {

			sendAction { IncrementAction }
			sendAction { DecrementAction }
			sendAction { DecrementAction }
			sendAction { IncrementAction }
			sendAction { IncrementAction }

			assertLastState { CounterState(counter = 1)}
			assertEventsCount(0)
			assertNoMoreEvents()
		}
	}

	@Test
	@RunAfter(InitialLifecycleState.ON_RESUME)
	fun testComplex() {
		reactorTest.runTest {

			assertNextState(CounterState(counter = 0))

			sendAction(DecrementAction)

			assertNextState(CounterState(counter = -1))

			sendAction(IncrementAction)

			assertNextState(CounterState(counter = 0))

			sendAction(IncrementAction)

			assertNextState(CounterState(counter = 1))

			sendAction(IncrementAction)

			assertNextState(CounterState(counter = 2))

			sendAction(IncrementAction)

			assertNextState(CounterState(counter = 3))

			sendAction(IncrementAction)

			assertNextState(CounterState(counter = 4))

			sendAction(IncrementAction)

			assertNextState(CounterState(counter = 5))

			sendAction(DecrementAction)

			assertNextState(CounterState(counter = 4))
			assertNextEvent(ShowNumberIsDivisibleByFiveToast)

			sendAction(IncrementAction)

			assertNextState(CounterState(counter = 5))
			assertNextEvent(ShowNumberIsDivisibleByFiveToast)

			sendAction { IncrementAction }
			sendAction { IncrementAction }
			sendAction { IncrementAction }

			assertLastState { CounterState(counter = 8) }

			fromOnResumeToOnDestroy()
			fromOnDestroyToOnResume()

			assertLastState { CounterState(counter = 8) }

			assertNoMoreInteractions()
		}
	}
}
