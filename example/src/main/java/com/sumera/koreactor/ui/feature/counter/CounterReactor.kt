package com.sumera.koreactor.ui.feature.counter

import com.sumera.koreactorlib.reactor.MviReactor
import com.sumera.koreactorlib.reactor.data.MviAction
import com.sumera.koreactor.ui.feature.counter.contract.CounterState
import com.sumera.koreactor.ui.feature.counter.contract.DecrementAction
import com.sumera.koreactor.ui.feature.counter.contract.DecrementReducer
import com.sumera.koreactor.ui.feature.counter.contract.IncrementAction
import com.sumera.koreactor.ui.feature.counter.contract.IncrementReducer
import com.sumera.koreactor.ui.feature.counter.contract.ShowNumberIsDivisibleByFiveToast
import com.sumera.koreactorlib.util.extension.getChange
import com.sumera.koreactor.injection.PerActivity
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
