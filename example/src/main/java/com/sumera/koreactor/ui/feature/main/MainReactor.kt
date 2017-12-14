package com.sumera.koreactor.ui.feature.main

import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.ui.feature.main.contract.*
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

		counterAction
				.map { NavigateToCounterEvent }
				.bindToView()

		todoAction
				.map { NavigateToToDoEvent }
				.bindToView()

		infinityAction
				.map { NavigateToInfinityEvent }
				.bindToView()
	}
}
