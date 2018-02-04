package com.sumera.koreactor.ui.feature.timer

import com.sumera.koreactor.lib.behaviour.implementation.TimerBehaviour
import com.sumera.koreactor.lib.behaviour.messages
import com.sumera.koreactor.lib.behaviour.triggers
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.ui.feature.timer.contract.IncrementCountReducer
import com.sumera.koreactor.ui.feature.timer.contract.ResetCountReducer
import com.sumera.koreactor.ui.feature.timer.contract.ResetTimerAction
import com.sumera.koreactor.ui.feature.timer.contract.TimerState
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerFragment
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
