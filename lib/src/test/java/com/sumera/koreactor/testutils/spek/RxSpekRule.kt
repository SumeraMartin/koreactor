package com.sumera.koreactor.testutils.spek

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSpekRule : TestRule {

    val scheduler: TestScheduler
        get() = testScheduler

    private var testScheduler = TestScheduler()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {

                before()
                try {
                    base.evaluate()
                } finally {
                    after()
                }
            }
        }
    }

    fun before() {
        testScheduler = TestScheduler()

        RxAndroidPlugins.reset()
        RxAndroidPlugins.setMainThreadSchedulerHandler { testScheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { testScheduler }

        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { testScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    }

    fun after() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }
}
