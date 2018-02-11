package com.sumera.koreactorexampleapp

import com.nhaarman.mockito_kotlin.whenever
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactorexampleapp.domain.GetSomeTextDataInteractor
import com.sumera.koreactorexampleapp.lib.ReactorTestRule
import com.sumera.koreactorexampleapp.lib.annotation.InitialLifecycleState
import com.sumera.koreactorexampleapp.lib.annotation.RunAfter
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.SimpleLoadingReactor
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.contract.RetryClicked
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.contract.SimpleLoadingState
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SimpleLoadingReactorTest {

	@Mock
	lateinit var mockGetSomeTextDataInteractor: GetSomeTextDataInteractor

	lateinit var getSomeTextDataSubject: PublishSubject<String>

	@Rule
	@JvmField
	val reactorTest	 = object : ReactorTestRule<SimpleLoadingState>() {
		override fun createNewReactorInstance(): MviReactor<SimpleLoadingState> {
			whenever(mockGetSomeTextDataInteractor.execute()).thenAnswer {
				getSomeTextDataSubject = PublishSubject.create<String>()
				return@thenAnswer Single.fromObservable(getSomeTextDataSubject.take(1))
			}
			return SimpleLoadingReactor(mockGetSomeTextDataInteractor)
		}
	}

	@Test
	@RunAfter(InitialLifecycleState.ON_RESUME)
	fun loadData_successfully_showData() {
		reactorTest.runTest {

			assertNextState { SimpleLoadingState(isLoading = true, isError = false, data = null) }

			trigger { getSomeTextDataSubject.onNext("TEST") }

			assertNextState { SimpleLoadingState(isLoading = false, isError = false, data = "TEST") }

			assertNoMoreInteractions()
		}
	}

	@Test
	@RunAfter(InitialLifecycleState.ON_RESUME)
	fun loadData_withError_showError() {
		reactorTest.runTest {

			assertNextState { SimpleLoadingState(isLoading = true, isError = false, data = null) }

			trigger { getSomeTextDataSubject.onError(IllegalStateException("")) }

			assertNextState { SimpleLoadingState(isLoading = false, isError = true, data = null) }

			assertNoMoreInteractions()
		}
	}

	@Test
	@RunAfter(InitialLifecycleState.ON_RESUME)
	fun loadData_withErrorAndSuccessfulRetryClick_showData() {
		reactorTest.runTest {

			assertNextState { SimpleLoadingState(isLoading = true, isError = false, data = null) }

			trigger { getSomeTextDataSubject.onError(IllegalStateException("")) }

			assertNextState { SimpleLoadingState(isLoading = false, isError = true, data = null) }

			sendAction { RetryClicked }

			assertNextState { SimpleLoadingState(isLoading = true, isError = false, data = null) }

			trigger { getSomeTextDataSubject.onNext("TEST") }

			assertNextState { SimpleLoadingState(isLoading = false, isError = false, data = "TEST") }

			assertNoMoreInteractions()
		}
	}

	@Test
	@RunAfter(InitialLifecycleState.ON_RESUME)
	fun loadData_withErrorAndErrorRetryClick_showErrorAgain() {
		reactorTest.runTest {

			assertNextState { SimpleLoadingState(isLoading = true, isError = false, data = null) }

			trigger { getSomeTextDataSubject.onError(IllegalStateException("")) }

			assertNextState { SimpleLoadingState(isLoading = false, isError = true, data = null) }

			sendAction { RetryClicked }

			assertNextState { SimpleLoadingState(isLoading = true, isError = false, data = null) }

			trigger { getSomeTextDataSubject.onError(IllegalStateException("")) }

			assertNextState { SimpleLoadingState(isLoading = false, isError = true, data = null) }

			assertNoMoreInteractions()
		}
	}
}