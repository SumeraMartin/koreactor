package com.sumera.koreactor.testutils

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxTestRule : TestRule {

    val scheduler: TestScheduler
        get() = testScheduler

    private var testScheduler = TestScheduler()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {

                initializeSchedulers()
                try {
                    base.evaluate()
                } finally {
                    resetSchedulers()
                }
            }
        }
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
}