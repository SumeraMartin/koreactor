package com.sumera.koreactorexampleapp.ui.feature.counter

import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.util.bundle.BundleWrapper
import com.sumera.koreactor.util.extension.getChange
import com.sumera.koreactorexampleapp.injection.PerActivity
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.CounterState
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.DecrementAction
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.DecrementReducer
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.IncrementAction
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.IncrementReducer
import com.sumera.koreactorexampleapp.ui.feature.counter.contract.ShowNumberIsDivisibleByFiveToast
import io.reactivex.Observable
import javax.inject.Inject

@PerActivity
class CounterReactor @Inject constructor(): MviReactor<CounterState>() {

	companion object {
	    private const val COUNTER_KEY = "counter_key"
	}

	override fun createInitialState(): CounterState {
		return CounterState(counter = 0)
	}

	override fun onSaveInstanceState(state: CounterState, bundleWrapper: BundleWrapper) {
		bundleWrapper.putInt(COUNTER_KEY, state.counter)
	}

	override fun onRestoreSaveInstanceState(state: CounterState, bundleWrapper: BundleWrapper): CounterState {
		val counter = bundleWrapper.getInt(COUNTER_KEY, 0)
		return state.copy(counter = counter)
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
