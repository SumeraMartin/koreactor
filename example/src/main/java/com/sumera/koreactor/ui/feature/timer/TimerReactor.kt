package com.sumera.koreactor.ui.feature.timer

import com.sumera.koreactor.injection.PerFragment
import com.sumera.koreactor.ui.feature.timer.contract.IncrementCountReducer
import com.sumera.koreactor.ui.feature.timer.contract.ResetCountReducer
import com.sumera.koreactor.ui.feature.timer.contract.ResetTimerAction
import com.sumera.koreactor.ui.feature.timer.contract.TimerState
import com.sumera.koreactorlib.behaviour.implementation.TimerBehaviour
import com.sumera.koreactorlib.behaviour.messages
import com.sumera.koreactorlib.behaviour.triggers
import com.sumera.koreactorlib.reactor.MviReactor
import com.sumera.koreactorlib.reactor.data.MviAction
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
                resetTrigger = triggers(resetAction),
                duration = 1,
                timeUnit = TimeUnit.SECONDS,
                tickMessage = messages({ IncrementCountReducer })
        ).bindToView()
    }
}
