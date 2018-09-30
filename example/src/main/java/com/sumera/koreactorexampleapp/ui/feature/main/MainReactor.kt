package com.sumera.koreactorexampleapp.ui.feature.main

import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactorexampleapp.injection.PerActivity
import com.sumera.koreactorexampleapp.ui.feature.main.contract.MainState
import com.sumera.koreactorexampleapp.ui.feature.main.contract.NavigateToCounterEvent
import com.sumera.koreactorexampleapp.ui.feature.main.contract.NavigateToInfinityEvent
import com.sumera.koreactorexampleapp.ui.feature.main.contract.NavigateToSimpleLoadingEvent
import com.sumera.koreactorexampleapp.ui.feature.main.contract.NavigateToTimerEvent
import com.sumera.koreactorexampleapp.ui.feature.main.contract.NavigateToToDoEvent
import com.sumera.koreactorexampleapp.ui.feature.main.contract.NavigateToToastsEvent
import com.sumera.koreactorexampleapp.ui.feature.main.contract.OnCounterButtonCLickAction
import com.sumera.koreactorexampleapp.ui.feature.main.contract.OnInfinityButtonClickAction
import com.sumera.koreactorexampleapp.ui.feature.main.contract.OnSimpleLoadingButtonClickAction
import com.sumera.koreactorexampleapp.ui.feature.main.contract.OnTimerButtonClickAction
import com.sumera.koreactorexampleapp.ui.feature.main.contract.OnToDoButtonClickAction
import com.sumera.koreactorexampleapp.ui.feature.main.contract.OnToastsButtonClickAction
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
		val toastAction = actions.ofActionType<OnToastsButtonClickAction>()

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

		toastAction
				.map { NavigateToToastsEvent }
				.bindToView()
	}
}
