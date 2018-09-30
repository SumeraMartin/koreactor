package com.sumera.koreactorexampleapp.ui.feature.timer

import com.sumera.koreactor.behaviour.implementation.TimerBehaviour
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.util.bundle.BundleWrapper
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

    companion object {
        private const val TIMER_VALUE_KEY = "timer_value_key"
    }

    override fun createInitialState(): TimerState {
        return TimerState(timerValue = 0)
    }

    override fun onSaveInstanceState(state: TimerState, bundleWrapper: BundleWrapper) {
        bundleWrapper.putInt(TIMER_VALUE_KEY, state.timerValue)
    }

    override fun onRestoreSaveInstanceState(state: TimerState, bundleWrapper: BundleWrapper): TimerState {
        val timerValue = bundleWrapper.getInt(TIMER_VALUE_KEY, 0)
        return state.copy(timerValue = timerValue)
    }

    override fun bind(actions: Observable<MviAction<TimerState>>) {
        val resetAction = actions.ofActionType<ResetTimerAction>()

        resetAction
                .map { ResetCountReducer }
                .bindToView()

        TimerBehaviour<TimerState>(
                initialTrigger = triggers(resetAction, startLifecycleObservable),
                cancelTrigger = triggers(resetAction, stopLifecycleObservable),
                duration = 1,
                timeUnit = TimeUnit.SECONDS,
                tickMessage = messages { IncrementCountReducer }
        ).bindToView()
    }
}
