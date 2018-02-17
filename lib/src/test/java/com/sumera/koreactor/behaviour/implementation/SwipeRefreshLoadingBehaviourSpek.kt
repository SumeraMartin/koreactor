package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.dispatch
import com.sumera.koreactor.behaviour.single
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.testutils.TestObserverWithOrder
import com.sumera.koreactor.testutils.spek.BehaviourSpekUtils
import com.sumera.koreactor.testutils.spek.TestError
import com.sumera.koreactor.testutils.spek.TestEvent
import com.sumera.koreactor.testutils.spek.TestState
import com.sumera.koreactor.testutils.spek.testDispatch
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class SwipeRefreshLoadingBehaviourSpek : Spek({

    val behaviourSpekUtils = BehaviourSpekUtils()
    lateinit var initialTrigger: PublishSubject<String>
    lateinit var swipeRefreshTrigger: PublishSubject<String>
    lateinit var firstWorkerSubject: PublishSubject<String>
    lateinit var secondWorkerSubject: PublishSubject<String>
    lateinit var firstSingleWorker: Single<String>
    lateinit var secondSingleWorker: Single<String>
    lateinit var testObserver: TestObserverWithOrder<MviReactorMessage<TestState>>

    fun prepareBehaviour() {
        initialTrigger = PublishSubject.create()
        swipeRefreshTrigger = PublishSubject.create()
        firstWorkerSubject = PublishSubject.create()
        secondWorkerSubject = PublishSubject.create()
        firstSingleWorker = Single.fromObservable(firstWorkerSubject.take(1))
        secondSingleWorker = Single.fromObservable(secondWorkerSubject.take(1))
        testObserver = TestObserverWithOrder()

        SwipeRefreshLoadingBehaviour<String, String, TestState>(
                initialTriggers = triggers(initialTrigger),
                swipeRefreshTriggers = triggers(swipeRefreshTrigger),
                loadWorker = single {
                    when(it.data) {
                        "in1" -> firstSingleWorker
                        "in2" -> secondSingleWorker
                        else -> throw IllegalStateException("Unknown worker")
                    }
                },
                onInitialLoading = dispatch { TestEvent("Init load ${it.data}") },
                onSwipeRefreshLoading = dispatch { TestEvent("Swipe load ${it.data}") },
                onInitialError = dispatch { TestEvent("Init error ${it.error.message}") },
                onSwipeRefreshError = dispatch { TestEvent("Swipe error ${it.error.message}") },
                onInitialData = dispatch { TestEvent("Init data ${it.output}") },
                onSwipeRefreshData = dispatch { TestEvent("Swipe data ${it.output}") }
        ).createObservable().subscribe(testObserver)
    }

    beforeEachTest {
        behaviourSpekUtils.before()
    }

    afterEachTest {
        behaviourSpekUtils.after()
    }

    describe("SwipeRefreshLoadingBehaviour") {

        beforeEachTest { prepareBehaviour(true) }

        with("swipe trigger") {
            beforeEachTest { swipeRefreshTrigger.onNext("in1") }

            it("should dispatch swipeLoad") {
                testObserver.assertValueAt(0, testDispatch(TestEvent("Swipe load in1")))
                testObserver.assertValueCount(1)
            }

            with("success worker response") {
                beforeEachTest { firstWorkerSubject.onNext("work1") }

                it("should dispatch swipeData") {
                    testObserver.assertValueAt(1, testDispatch(TestEvent("Swipe data work1")))
                    testObserver.assertValueCount(2)
                }

                with("swipe trigger") {
                    beforeEachTest { swipeRefreshTrigger.onNext("in2") }

                    it("should dispatch swipeLoad") {
                        testObserver.assertValueAt(2, testDispatch(TestEvent("Swipe load in2")))
                        testObserver.assertValueCount(3)
                    }

                    with("success worker resposne") {
                        beforeEachTest { secondWorkerSubject.onNext("work2") }

                        it("should dispatch swipe data") {
                            testObserver.assertValueAt(3, testDispatch(TestEvent("Swipe data work2")))
                            testObserver.assertValueCount(4)
                        }
                    }

                    with("error worker resposne") {
                        beforeEachTest { secondWorkerSubject.onError(TestError()) }

                        it("should dispatch swipe data") {
                            testObserver.assertValueAt(3, testDispatch(TestEvent("Swipe error TestError")))
                            testObserver.assertValueCount(4)
                        }
                    }
                }
            }

            with("error worker response") {
                beforeEachTest { firstWorkerSubject.onError(TestError()) }

                it("should dispatch swipeData") {
                    testObserver.assertValueAt(1, testDispatch(TestEvent("Swipe error TestError")))
                    testObserver.assertValueCount(2)
                }

                with("swipe trigger") {
                    beforeEachTest { swipeRefreshTrigger.onNext("in2") }

                    it("should dispatch swipeLoad") {
                        testObserver.assertValueAt(2, testDispatch(TestEvent("Swipe load in2")))
                        testObserver.assertValueCount(3)
                    }

                    with("success worker response") {
                        beforeEachTest { secondWorkerSubject.onNext("work2") }

                        it("should dispatch swipe data") {
                            testObserver.assertValueAt(3, testDispatch(TestEvent("Swipe data work2")))
                            testObserver.assertValueCount(4)
                        }
                    }

                    with("error worker response") {
                        beforeEachTest { secondWorkerSubject.onError(TestError()) }

                        it("should dispatch swipe data") {
                            testObserver.assertValueAt(3, testDispatch(TestEvent("Swipe error TestError")))
                            testObserver.assertValueCount(4)
                        }
                    }
                }
            }

            with("another initial trigger") {
                beforeEachTest { initialTrigger.onNext("in2") }

                it("should dispatch initLoad") {
                    testObserver.assertValueAt(1, testDispatch(TestEvent("Init load in2")))
                    testObserver.assertValueCount(2)
                }

                with("success worker response") {
                    beforeEachTest { secondWorkerSubject.onNext("work2") }

                    it("should dispatch initData") {
                        testObserver.assertValueAt(2, testDispatch(TestEvent("Init data work2")))
                        testObserver.assertValueCount(3)
                    }
                }

                with("error worker response") {
                    beforeEachTest { secondWorkerSubject.onError(TestError()) }

                    it("should dispatch initError") {
                        testObserver.assertValueAt(2, testDispatch(TestEvent("Init error TestError")))
                        testObserver.assertValueCount(3)
                    }
                }
            }

            with("another swipe trigger") {
                beforeEachTest { swipeRefreshTrigger.onNext("in2") }

                it("should dispatch swipeLoad") {
                    testObserver.assertValueAt(1, testDispatch(TestEvent("Swipe load in2")))
                    testObserver.assertValueCount(2)
                }

                with("success worker response") {
                    beforeEachTest { secondWorkerSubject.onNext("work2") }

                    it("should dispatch swipeData") {
                        testObserver.assertValueAt(2, testDispatch(TestEvent("Swipe data work2")))
                        testObserver.assertValueCount(3)
                    }
                }

                with("error worker response") {
                    beforeEachTest { secondWorkerSubject.onError(TestError()) }

                    it("should dispatch swipeError") {
                        testObserver.assertValueAt(2, testDispatch(TestEvent("Swipe error TestError")))
                        testObserver.assertValueCount(3)
                    }
                }
            }
        }

        with("initial trigger") {
            beforeEachTest { initialTrigger.onNext("in1") }

            it("should dispatch initLoad") {
                testObserver.assertValueAt(0, testDispatch(TestEvent("Init load in1")))
                testObserver.assertValueCount(1)
            }

            with("success response") {
                beforeEachTest { firstWorkerSubject.onNext("work1") }

                it("should dispatch initData") {
                    testObserver.assertValueAt(1, testDispatch(TestEvent("Init data work1")))
                    testObserver.assertValueCount(2)
                }
            }

            with("error response") {
                beforeEachTest { firstWorkerSubject.onError(TestError()) }

                it("should dispatch initError") {
                    testObserver.assertValueAt(1, testDispatch(TestEvent("Init error TestError")))
                    testObserver.assertValueCount(2)
                }
            }

            with("another initial trigger") {
                beforeEachTest { initialTrigger.onNext("in2") }

                it("should dispatch initLoad") {
                    testObserver.assertValueAt(1, testDispatch(TestEvent("Init load in2")))
                    testObserver.assertValueCount(2)
                }

                with("success worker response") {
                    beforeEachTest { secondWorkerSubject.onNext("work2") }

                    it("should dispatch initData") {
                        testObserver.assertValueAt(2, testDispatch(TestEvent("Init data work2")))
                        testObserver.assertValueCount(3)
                    }
                }

                with("error worker response") {
                    beforeEachTest { secondWorkerSubject.onError(TestError()) }

                    it("should dispatch initError") {
                        testObserver.assertValueAt(2, testDispatch(TestEvent("Init error TestError")))
                        testObserver.assertValueCount(3)
                    }
                }
            }

            with("another swipe trigger") {
                beforeEachTest { swipeRefreshTrigger.onNext("in2") }

                it("should dispatch swipeLoad") {
                    testObserver.assertValueAt(1, testDispatch(TestEvent("Swipe load in2")))
                    testObserver.assertValueCount(2)
                }

                with("success worker response") {
                    beforeEachTest { secondWorkerSubject.onNext("work2") }

                    it("should dispatch swipeData") {
                        testObserver.assertValueAt(2, testDispatch(TestEvent("Swipe data work2")))
                        testObserver.assertValueCount(3)
                    }
                }

                with("error worker response") {
                    beforeEachTest { secondWorkerSubject.onError(TestError()) }

                    it("should dispatch swipeError") {
                        testObserver.assertValueAt(2, testDispatch(TestEvent("Swipe error TestError")))
                        testObserver.assertValueCount(3)
                    }
                }
            }
        }
    }
})

fun SpecBody.with(description: String, body: SpecBody.() -> Unit) {
    group("with $description", body = body)
}

fun SpecBody.followedBy(description: String, body: SpecBody.() -> Unit) {
    group("followed by $description", body = body)
}


