package com.sumera.koreactorexampleapp.ui.feature.counter

import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.CounterState
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.DecrementAction
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.DecrementReducer
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.IncrementAction
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.IncrementReducer
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.ShowNumberIsDivisibleByFiveToast
import com.sumera.koreactor.util.extension.getChange
import com.sumera.koreactorexampleapp.injection.PerActivity
import io.reactivex.Observable
import javax.inject.Inject

@PerActivity
class CounterReactor @Inject constructor(): MviReactor<CounterState>() {

	override fun createInitialState(): CounterState {
		return CounterState(counter = 0)
	}

	override fun bind(actions: Observable<MviAction<CounterState>>) {
		val incrementAction = actions.ofActionType<IncrementAction>()
		val decrementAction = actions.ofActionType<DecrementAction>()

		incrementAction
				.map { IncrementReducer }
				.bindToView()

		decrementAction
				.map { DecrementReducer }
				.bindToView()

		stateObservable
				.getChange { it.counter }
				.filter { it % 5 == 0 && it != 0 }
				.map { ShowNumberIsDivisibleByFiveToast }
				.bindToView()
	}
}
