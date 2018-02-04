package com.sumera.koreactor.lib

import com.sumera.koreactor.lib.annotation.InitialLifecycleState
import com.sumera.koreactor.lib.annotation.RunAfter
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviState
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

abstract class ReactorTestRule<STATE : MviState> : TestRule {

	abstract fun createNewReactorInstance(): MviReactor<STATE>

	val scheduler: TestScheduler
		get() = testScheduler

	val testWrapper: ReactorTestWrapper<STATE>
		get() = reactorTestWrapper

	private var testScheduler = TestScheduler()

	private lateinit var reactor: MviReactor<STATE>

	private lateinit var testView: TestMviBindableDelegate<STATE>

	private lateinit var reactorTestWrapper: ReactorTestWrapper<STATE>

	override fun apply(base: Statement, description: Description): Statement {
		return object : Statement() {
			@Throws(Throwable::class)
			override fun evaluate() {

				initializeSchedulers()
				initializeReactor()

				try {
					runWithinScope(getRunAfterAnnotation(description), base)
					reactorTestWrapper.assertFinalReactorState()
				} finally {
					resetSchedulers()
				}
			}
		}
	}

	fun runTest(testBlock: ReactorTestWrapper<STATE>.() -> Unit) {
		testWrapper.testBlock()
	}

	private fun initializeReactor() {
		reactor = createNewReactorInstance()
		testView = TestMviBindableDelegate()
		reactorTestWrapper = ReactorTestWrapper(reactor, testView, testScheduler)
	}

	private fun initializeSchedulers() {
		testScheduler = TestScheduler()

		RxAndroidPlugins.reset()
		RxAndroidPlugins.setMainThreadSchedulerHandler { testScheduler }
		RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }

		RxJavaPlugins.reset()
		RxJavaPlugins.setIoSchedulerHandler { testScheduler }
		RxJavaPlugins.setNewThreadSchedulerHandler { testScheduler }
		RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
	}

	private fun resetSchedulers() {
		RxAndroidPlugins.reset()
		RxJavaPlugins.reset()
	}

	private fun getRunAfterAnnotation(description: Description) : RunAfter {
		val runAfterAnnotationsCount = description.annotations.count { it is RunAfter }
		if (runAfterAnnotationsCount == 0) {
			throw IllegalStateException("Every reactor test must contains @RunAfter annotation")
		}
		return description.annotations.find { it is RunAfter } as RunAfter
	}

	private fun runWithinScope(runAfterAnnotation: RunAfter, base: Statement) {
		when (runAfterAnnotation.initialLifecycleState) {
			InitialLifecycleState.ON_CREATE -> {
				testWrapper.runTestCaseAfterOnCreate { base.evaluate() }
			}
			InitialLifecycleState.ON_START -> {
				testWrapper.runTestCaseAfterOnStart { base.evaluate() }
			}
			InitialLifecycleState.ON_RESUME -> {
				testWrapper.runTestCaseAfterOnResume { base.evaluate() }
			}
			InitialLifecycleState.UNINITIALIZED -> {
				testWrapper.runTestCaseWithoutAnyLifecycleCalls { base.evaluate() }
			}
		}
	}
}