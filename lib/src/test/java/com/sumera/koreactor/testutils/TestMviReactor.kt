package com.sumera.koreactor.testutils

import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.LifecycleState
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.reactor.data.MviStateReducer
import com.sumera.koreactor.util.bundle.BundleWrapper
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject

class TestMviReactor : MviReactor<TestState>() {

    var actionTestObserver = TestObserver<MviAction<TestState>>()

    var lifecycleTestObserver = TestObserver<LifecycleState>()

    var testSubject = PublishSubject.create<MviStateReducer<TestState>>()

    private var bindCallsCount = 0

    private var createInitialStateCallsCount = 0

    private var onSaveInstanceStateCallsCount = 0

    private var onRestoreSaveInstanceStateCallsCount = 0

    private var actionSubject = PublishSubject.create<MviAction<TestState>>()

    private var lifecycleSubject = PublishSubject.create<LifecycleState>()

    init {
        actionSubject.subscribe(actionTestObserver)
        lifecycleSubject.subscribe(lifecycleTestObserver)
    }

    override fun createInitialState(): TestState {
        createInitialStateCallsCount++
        return TestState()
    }

    override fun bind(actions: Observable<MviAction<TestState>>) {
        actions.subscribe(actionSubject)
        lifecycleObservable.subscribe(lifecycleSubject)

        testSubject.bindToView()

        bindCallsCount++
    }

    override fun onSaveInstanceState(state: TestState, bundleWrapper: BundleWrapper) {
        onSaveInstanceStateCallsCount++
    }

    override fun onRestoreSaveInstanceState(state: TestState, bundleWrapper: BundleWrapper): TestState {
        onRestoreSaveInstanceStateCallsCount++
        return state
    }

    fun assertCreateInitialStateCallsCount (expectedCreateInitialStateCallsCount : Int) {
        if (createInitialStateCallsCount != expectedCreateInitialStateCallsCount) {
            throw AssertionError("Expected expectedCreateInitialStateCallsCount: $expectedCreateInitialStateCallsCount actual: $createInitialStateCallsCount")
        }
    }

    fun assertBindCallsCount(expectedBindCallsCount: Int) {
        if (bindCallsCount != expectedBindCallsCount) {
            throw AssertionError("Expected expectedBindCallsCount: $expectedBindCallsCount actual: $bindCallsCount")
        }
    }

    fun assertOnSaveInstanceStateCallsCount(expectedOnSaveInstanceStateCallsCount: Int) {
        if (onSaveInstanceStateCallsCount != expectedOnSaveInstanceStateCallsCount) {
            throw AssertionError("Expected expectedOnSaveInstanceStateCallsCount: $expectedOnSaveInstanceStateCallsCount actual: $onSaveInstanceStateCallsCount")
        }
    }

    fun assertOnRestoreSaveInstanceStateCallsCount(expectedOnRestoreSaveInstanceStateCallsCount: Int) {
        if (onRestoreSaveInstanceStateCallsCount != expectedOnRestoreSaveInstanceStateCallsCount) {
            throw AssertionError("Expected expectedOnRestoreSaveInstanceStateCallsCount: $expectedOnRestoreSaveInstanceStateCallsCount actual: $onRestoreSaveInstanceStateCallsCount")
        }
    }
}
