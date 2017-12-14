package com.sumera.koreactor.ui.feature.main

import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding2.view.clicks
import com.sumera.koreactor.R
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.event.MviEvent
import com.sumera.koreactor.ui.base.BaseActivity
import com.sumera.koreactor.ui.feature.counter.CounterActivity
import com.sumera.koreactor.ui.feature.main.contract.*
import com.sumera.koreactor.ui.feature.todo.InfinityActivity
import com.sumera.koreactor.ui.feature.todo.ToDoActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainState>() {

	@Inject lateinit var viewModelFactory: MainReactorFactory

	override val layoutRes: Int
		get() = R.layout.activity_main

	override val reactor: MviReactor<MainState>
		get() = getReactor(viewModelFactory, MainReactor::class.java)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		main_infinity.clicks()
				.map { OnInfinityButtonClickAction }
				.bindToReactor()

		main_todo.clicks()
				.map { OnToDoButtonClickAction }
				.bindToReactor()

		main_counter.clicks()
				.map { OnCounterButtonCLickAction }
				.bindToReactor()
	}

	override fun bindToState(stateObservable: Observable<MainState>) {
		// Empty state
	}

	override fun bindToEvent(eventsObservable: Observable<MviEvent<MainState>>) {
		eventsObservable.observe { event ->
			when (event) {
				is NavigateToInfinityEvent -> startActivity(Intent(this, InfinityActivity::class.java))
				is NavigateToCounterEvent -> startActivity(Intent(this, CounterActivity::class.java))
				is NavigateToToDoEvent -> startActivity(Intent(this, ToDoActivity::class.java))
			}
		}
	}


}
