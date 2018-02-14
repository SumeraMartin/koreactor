package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.Messages
import com.sumera.koreactor.behaviour.MviBehaviour
import com.sumera.koreactor.behaviour.Triggers
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

data class IntervalBehaviour<STATE : MviState>(
        private val triggers: Triggers<out Any>,
        private val cancelTriggers: Triggers<out Any> = triggers(),
        private val duration: Long,
        private val timeUnit: TimeUnit,
        private val onTick: Messages<Long, STATE>
): MviBehaviour<STATE> {

    override fun createObservable(): Observable<MviReactorMessage<STATE>> {
        return triggers.merge()
                .switchMap { Observable
                        .interval(duration, timeUnit)
                        .takeUntil(cancelTriggers.merge())
                }
                .map { onTick.applyData(it) }
    }
}
