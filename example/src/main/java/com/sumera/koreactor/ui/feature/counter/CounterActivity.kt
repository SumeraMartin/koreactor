package com.sumera.koreactor.ui.feature.counter

import android.os.Bundle
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import com.sumera.koreactor.R
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.ui.base.BaseActivity
import com.sumera.koreactor.ui.feature.counter.contract.CounterState
import com.sumera.koreactor.ui.feature.counter.contract.DecrementAction
import com.sumera.koreactor.ui.feature.counter.contract.IncrementAction
import com.sumera.koreactor.ui.feature.counter.contract.ShowNumberIsDivisibleByFiveToast
import com.sumera.koreactor.util.extension.getChange
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_counter.*
import javax.inject.Inject

class CounterActivity: BaseActivity<CounterState>() {

	@Inject lateinit var reactorFactory: CounterReactorFactory

	override val layoutRes: Int
		get() = R.layout.activity_counter

	override fun createReactor(): MviReactor<CounterState> {
		return getReactor(reactorFactory, CounterReactor::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		counter_increment.clicks()
				.map { IncrementAction }
				.bindToReactor()

		counter_decrement.clicks()
				.map { DecrementAction }
				.bindToReactor()
	}

	override fun bindToState(stateObservable: Observable<CounterState>) {
		stateObservable
				.getChange { it.counter }
				.observeState { counter_countText.text = it.toString() }
	}

	override fun bindToEvent(eventsObservable: Observable<MviEvent<CounterState>>) {
		eventsObservable.observeEvent { event ->
			when(event) {
				is ShowNumberIsDivisibleByFiveToast ->
					Toast.makeText(this, "Is divisible by 5", Toast.LENGTH_SHORT).show()
			}
		}
	}
}
