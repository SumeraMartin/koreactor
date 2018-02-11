package com.sumera.koreactor.testutils

import com.sumera.koreactor.behaviour.MviBehaviour
import com.sumera.koreactor.behaviour.Triggers
import com.sumera.koreactor.reactor.data.MviReactorMessage
import io.reactivex.Observable

data class TestMviBehaviour(
        val testTriggers: Triggers<MviReactorMessage<TestState>>
) : MviBehaviour<TestState> {

    override fun createObservable(): Observable<out MviReactorMessage<TestState>> {
        return testTriggers.merge()
    }
}