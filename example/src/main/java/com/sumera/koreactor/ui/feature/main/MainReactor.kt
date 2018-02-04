package com.sumera.koreactor.ui.feature.main

import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.ui.feature.main.contract.MainState
import com.sumera.koreactor.ui.feature.main.contract.NavigateToCounterEvent
import com.sumera.koreactor.ui.feature.main.contract.NavigateToInfinityEvent
import com.sumera.koreactor.ui.feature.main.contract.NavigateToSimpleLoadingEvent
import com.sumera.koreactor.ui.feature.main.contract.NavigateToTimerEvent
import com.sumera.koreactor.ui.feature.main.contract.NavigateToToDoEvent
import com.sumera.koreactor.ui.feature.main.contract.OnCounterButtonCLickAction
import com.sumera.koreactor.ui.feature.main.contract.OnInfinityButtonClickAction
import com.sumera.koreactor.ui.feature.main.contract.OnSimpleLoadingButtonClickAction
import com.sumera.koreactor.ui.feature.main.contract.OnTimerButtonClickAction
import com.sumera.koreactor.ui.feature.main.contract.OnToDoButtonClickAction
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import io.reactivex.Observable
import javax.inject.Inject

@PerActivity
class MainReactor @Inject constructor() : MviReactor<MainState>() {

	override fun createInitialState(): MainState {
		return MainState
	}

	override fun bind(actions: Observable<MviAction<MainState>>) {
		val counterAction = actions.ofActionType<OnCounterButtonCLickAction>()
		val todoAction = actions.ofActionType<OnToDoButtonClickAction>()
		val infinityAction = actions.ofActionType<OnInfinityButtonClickAction>()
		val simpleLoadingAction = actions.ofActionType<OnSimpleLoadingButtonClickAction>()
		val timerAction = actions.ofActionType<OnTimerButtonClickAction>()

		counterAction
				.map { NavigateToCounterEvent }
				.bindToView()

		todoAction
				.map { NavigateToToDoEvent }
				.bindToView()

		infinityAction
				.map { NavigateToInfinityEvent }
				.bindToView()

		simpleLoadingAction
				.map { NavigateToSimpleLoadingEvent }
				.bindToView()

		timerAction
				.map { NavigateToTimerEvent }
				.bindToView()
	}
}
