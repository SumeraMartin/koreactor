package com.sumera.koreactorexampleapp.ui.feature.timer

import com.sumera.koreactor.behaviour.implementation.TimerBehaviour
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactorexampleapp.injection.PerFragment
import com.sumera.koreactorexampleapp.ui.feature.timer.contract.IncrementCountReducer
import com.sumera.koreactorexampleapp.ui.feature.timer.contract.ResetCountReducer
import com.sumera.koreactorexampleapp.ui.feature.timer.contract.ResetTimerAction
import com.sumera.koreactorexampleapp.ui.feature.timer.contract.TimerState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerFragment
class TimerReactor @Inject constructor() : MviReactor<TimerState>() {

    override fun createInitialState(): TimerState {
        return TimerState(timerValue = 0)
    }

    override fun bind(actions: Observable<MviAction<TimerState>>) {
        val resetAction = actions.ofActionType<ResetTimerAction>()

        resetAction
                .map { ResetCountReducer }
                .bindToView()

        TimerBehaviour<TimerState>(
                initialTrigger = triggers(attachLifecycleObservable),
                cancelTrigger = triggers(resetAction),
                duration = 1,
                timeUnit = TimeUnit.SECONDS,
                tickMessage = messages({ IncrementCountReducer })
        ).bindToView()
    }
}
