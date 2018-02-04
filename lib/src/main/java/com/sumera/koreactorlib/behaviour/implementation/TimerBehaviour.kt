package com.sumera.koreactorlib.behaviour.implementation

import com.sumera.koreactorlib.behaviour.Messages
import com.sumera.koreactorlib.behaviour.MviBehaviour
import com.sumera.koreactorlib.behaviour.Triggers
import com.sumera.koreactorlib.reactor.data.MviReactorMessage
import com.sumera.koreactorlib.reactor.data.MviState
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
