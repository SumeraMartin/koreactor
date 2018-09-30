package com.sumera.koreactorexampleapp.ui.feature.timer

import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.util.extension.getChange
import com.sumera.koreactorexampleapp.R
import com.sumera.koreactorexampleapp.ui.base.BaseFragment
import com.sumera.koreactorexampleapp.ui.feature.timer.contract.ResetTimerAction
import com.sumera.koreactorexampleapp.ui.feature.timer.contract.TimerState
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_timer.*
import javax.inject.Inject

class TimerFragment: BaseFragment<TimerState>() {

    @Inject lateinit var reactorFactory: TimerReactorFactory

    override fun createReactor(): MviReactor<TimerState> {
       return getReactor(reactorFactory, TimerReactor::class.java)
    }

    override val layoutRes: Int
        get() = R.layout.fragment_timer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timer_reset.clicks()
                .map { ResetTimerAction }
                .bindToReactor()
    }

    override fun bindToState(stateObservable: Observable<TimerState>) {
        stateObservable
                .getChange { it.timerValue }
                .observeState {
                    timer_countText.text = it.toString()
                }
    }
}
