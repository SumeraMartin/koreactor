package com.sumera.koreactorexampleapp.ui.feature.infinity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.util.data.asOptional
import com.sumera.koreactor.util.extension.getChange
import com.sumera.koreactor.util.extension.getTrue
import com.sumera.koreactorexampleapp.R
import com.sumera.koreactorexampleapp.ui.base.BaseActivity
import com.sumera.koreactorexampleapp.ui.common.PlaceholderLayout
import com.sumera.koreactorexampleapp.ui.feature.infinity.adapter.InfinityAdapter
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.InfinityState
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.NavigateToDetailEvent
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.OnItemClickedAction
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.OnRetryInfinityLoadingAction
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.OnRetryInitialAction
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.OnScrolledToBottomAction
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_infinity.*
import javax.inject.Inject

class InfinityActivity : BaseActivity<InfinityState>() {

	@Inject lateinit var reactorFactory: InfinityReactorFactory
	@Inject lateinit var adapter: InfinityAdapter

	override val layoutRes: Int
		get() = R.layout.activity_infinity

	override fun createReactor(): MviReactor<InfinityState> {
		return getReactor(reactorFactory, InfinityReactor::class.java)
	}

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

		// Show triggerInput
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