package com.sumera.koreactor.lib.behaviour.implementation

import com.sumera.koreactor.lib.behaviour.Messages
import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.behaviour.Triggers
import com.sumera.koreactor.lib.reactor.data.MviReactorMessage
import com.sumera.koreactor.lib.reactor.data.MviState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

data class TimerBehaviour<STATE : MviState>(
        private val initialTrigger: Triggers<Any>,
        private val resetTrigger: Triggers<Any>,
        private val duration: Long,
        private val timeUnit: TimeUnit,
        private val tickMessage: Messages<Long, STATE>
): MviBehaviour<STATE> {

    override fun createObservable(): Observable<MviReactorMessage<STATE>> {
        return Observable.merge(initialTrigger.observables + resetTrigger.observables)
                .switchMap { Observable.interval(duration, timeUnit) }
                .map { tickMessage.applyData(it) }
    }
}
