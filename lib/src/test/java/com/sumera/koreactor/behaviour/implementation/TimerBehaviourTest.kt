package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.dispatch
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.testutils.BaseBehaviourTest
import com.sumera.koreactor.testutils.it
import com.sumera.koreactor.testutils.on
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class TimerBehaviourTest : BaseBehaviourTest() {

    lateinit var initialTrigger: PublishSubject<Input>

    lateinit var cancelSubject: PublishSubject<Input>

    fun createBehaviour(cancelPrevious: Boolean) {
        TimerBehaviour<Input, TestState>(
                triggers = triggers(initialTrigger),
                duration = 2,
                timeUnit = TimeUnit.SECONDS,
                cancelPrevious = cancelPrevious,
                onStart = dispatch({ Output(id = "Start " + it.id) }),
                onEnd = dispatch({ Output(id = "End " + it.id) })
        ).createObservable().subscribe(testObserver)
    }

    @Before
    fun before() {
        initialTrigger = PublishSubject.create()
        cancelSubject = PublishSubject.create()
        testObserver = TestObserver()
    }

    @After
    fun after() {
        testObserver.assertNotTerminated()
        testObserver.assertNoErrors()
    }

    @Test
    fun `with single trigger action`() {
        createBehaviour(true)

        on("First triggered action") {
            initialTrigger.onNext(Input("Test"))

            it("dispatch start message") {
                testObserver.assertValueCount(1)
                testObserver.assertValueAt(0, testMessage(Output(id = "Start Test")))
            }
        }

        on("After one second") {
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            it("dispatch no new message") {
                testObserver.assertValueCount(1)
            }
        }

        on("After one second") {
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            it("dispatch end message") {
                testObserver.assertValueCount(2)
                testObserver.assertValueAt(1, testMessage(Output(id = "End Test")))
            }
        }
    }

    @Test
    fun `with cancel previous and multiple trigger actions before end`() {
        createBehaviour(true)

        on("First time triggered") {
            initialTrigger.onNext(Input("Test1"))

            it("dispatch start message") {
                testObserver.assertValueCount(1)
                testObserver.assertValueAt(0, testMessage(Output(id = "Start Test1")))
            }
        }

        on("After one second") {
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            it("dispatch nothing new") {
                testObserver.assertValueCount(1)
            }
        }

        on("Second time triggered") {
            initialTrigger.onNext(Input("Test2"))

            it("dispatch another start message") {
                testObserver.assertValueCount(2)
                testObserver.assertValueAt(1, testMessage(Output(id = "Start Test2")))
            }
        }

        on("After one seconds") {
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            it("dispatch nothing new") {
                testObserver.assertValueCount(2)
            }
        }

        on("After one seconds=") {
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            it("dispatch end message") {
                testObserver.assertValueCount(3)
                testObserver.assertValueAt(2, testMessage(Output(id = "End Test2")))
            }
        }
    }

    @Test
    fun `with cancel previous and multiple trigger actions after end`() {
        createBehaviour(true)

        on("First time triggered") {
            initialTrigger.onNext(Input("Test1"))

            it("dispatch start message") {
                testObserver.assertValueCount(1)
                testObserver.assertValueAt(0, testMessage(Output(id = "Start Test1")))
            }
        }

        on("After two seconds") {
            scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

            it("dispatch end message") {
                testObserver.assertValueCount(2)
                testObserver.assertValueAt(1, testMessage(Output(id = "End Test1")))
            }
        }

        on("After ten seconds") {
            scheduler.advanceTimeBy(10, TimeUnit.SECONDS)

            it("dispatch nothing new") {
                testObserver.assertValueCount(2)
            }
        }

        on("Second time triggered") {
            initialTrigger.onNext(Input("Test2"))

            it("dispatch start message") {
                testObserver.assertValueCount(3)
                testObserver.assertValueAt(2, testMessage(Output(id = "Start Test2")))
            }
        }

        on("After two seconds") {
            scheduler.advanceTimeBy(2, TimeUnit.SECONDS)

            it("dispatch end message") {
                testObserver.assertValueCount(4)
                testObserver.assertValueAt(3, testMessage(Output(id = "End Test2")))
            }
        }

        on("After ten seconds") {
            scheduler.advanceTimeBy(10, TimeUnit.SECONDS)

            it("dispatch nothing new") {
                testObserver.assertValueCount(4)
            }
        }
    }

    @Test
    fun `without cancel previous and with multiple trigger actions before end`() {
        createBehaviour(false)

        on("First time triggered") {
            initialTrigger.onNext(Input("Test1"))

            it("dispatch first start message") {
                testObserver.assertValueCount(1)
                testObserver.assertValueAt(0, testMessage(Output(id = "Start Test1")))
            }
        }

        on("After one second") {
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            it("dispatch nothing new") {
                testObserver.assertValueCount(1)
            }
        }

        on("Second time triggered") {
            initialTrigger.onNext(Input("Test2"))

            it("dispatch second start message") {
                testObserver.assertValueCount(2)
                testObserver.assertValueAt(1, testMessage(Output(id = "Start Test2")))
            }
        }

        on("After one seconds") {
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            it("dispatch first end message") {
                testObserver.assertValueCount(3)
                testObserver.assertValueAt(2, testMessage(Output(id = "End Test1")))
            }
        }

        on("After one seconds") {
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            it("dispatch second end message") {
                testObserver.assertValueCount(4)
                testObserver.assertValueAt(3, testMessage(Output(id = "End Test2")))
            }
        }
    }
}