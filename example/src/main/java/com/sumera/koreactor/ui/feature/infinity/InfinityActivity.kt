package com.sumera.koreactor.ui.feature.todo

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.sumera.koreactor.R
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.event.MviEvent
import com.sumera.koreactor.lib.util.data.asOptional
import com.sumera.koreactor.lib.util.extension.getChange
import com.sumera.koreactor.lib.util.extension.getTrue
import com.sumera.koreactor.ui.base.BaseActivity
import com.sumera.koreactor.ui.common.PlaceholderLayout
import com.sumera.koreactor.ui.feature.infinity.adapter.InfinityAdapter
import com.sumera.koreactor.ui.feature.infinity.contract.*
import com.sumera.koreactor.ui.feature.todo.contract.InfinityState
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_infinity.*
import javax.inject.Inject

class InfinityActivity : BaseActivity<InfinityState>() {

	@Inject lateinit var viewModelFactory: InfinityReactorFactory
	@Inject lateinit var adapter: InfinityAdapter

	override val layoutRes: Int
		get() = R.layout.activity_infinity

	override val reactor: MviReactor<InfinityState>
		get() = getReactor(viewModelFactory, InfinityReactor::class.java)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		infinity_recycler.layoutManager = LinearLayoutManager(this)
		infinity_recycler.adapter = adapter

		adapter.toDoItemClicks()
				.map { OnItemClickedAction(it) }
				.bindToReactor()

		adapter.bottomScrooled()
				.map { OnScrolledToBottomAction }
				.bindToReactor()

		adapter.retryClicked()
				.map { OnRetryInfinityLoadingAction }
				.bindToReactor()

		infinity_placeholder.retryClicks()
				.map { OnRetryInitialAction }
				.bindToReactor()
	}

	override fun bindToState(stateObservable: Observable<InfinityState>) {
		// Show error layout
		stateObservable
				.getTrue { it.isInitialError }
				.observeState { infinity_placeholder.show(PlaceholderLayout.NETWORK_ERROR) }

		// Show loading layout
		stateObservable
				.getTrue { it.isInitialLoading }
				.observeState { infinity_placeholder.show(PlaceholderLayout.LOADING) }

		// Show infinity loading
		stateObservable
				.getChange { it.isInfinityLoading }
				.observeState { adapter.setLoading(it) }

		// Show infinity error
		stateObservable
				.getChange { it.isInfinityError }
				.observeState { adapter.setError(it) }

		// Show data
		stateObservable
				.getChange { it.data.asOptional() }
				.filter { it.value?.isNotEmpty() ?: false }
				.observeState {
					infinity_placeholder.hide()
					adapter.data = it.value!!
				}
	}

	override fun bindToEvent(eventsObservable: Observable<MviEvent<InfinityState>>) {
		eventsObservable.observeEvent { event ->
			when(event) {
				is NavigateToDetailEvent ->
					finish()
			}
		}
	}
}