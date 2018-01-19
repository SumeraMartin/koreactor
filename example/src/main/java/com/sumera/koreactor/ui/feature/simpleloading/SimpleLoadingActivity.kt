package com.sumera.koreactor.ui.feature.simpleloading

import android.os.Bundle
import com.sumera.koreactor.R
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.event.MviEvent
import com.sumera.koreactor.lib.util.data.asOptional
import com.sumera.koreactor.lib.util.extension.getFalse
import com.sumera.koreactor.lib.util.extension.getNotNull
import com.sumera.koreactor.lib.util.extension.getTrue
import com.sumera.koreactor.ui.base.BaseActivity
import com.sumera.koreactor.ui.common.PlaceholderLayout
import com.sumera.koreactor.ui.feature.simpleloading.contract.RetryClicked
import com.sumera.koreactor.ui.feature.simpleloading.contract.SimpleLoadingState
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_simple_loading.*

class SimpleLoadingActivity : BaseActivity<SimpleLoadingState>() {

	override val layoutRes: Int
		get() = R.layout.activity_simple_loading

	override val reactor: MviReactor<SimpleLoadingState>
		get() = TODO("not implemented")

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		simpleLoading_placeholder.retryClicks()
				.map { RetryClicked }
				.bindToReactor()
	}

	override fun bindToState(stateObservable: Observable<SimpleLoadingState>) {
		stateObservable
				.getTrue { it.isLoading }
				.observeState { simpleLoading_placeholder.show(PlaceholderLayout.LOADING) }

		stateObservable
				.getTrue { it.isError }
				.observeState { simpleLoading_placeholder.show(PlaceholderLayout.NETWORK_ERROR) }

		stateObservable
				.getFalse { it.isError || it.isLoading }
				.observeState { simpleLoading_placeholder.hide() }

		stateObservable
				.getNotNull { it.data.asOptional() }
				.observeState { simpleLoading_dataText.text = it }
	}

	override fun bindToEvent(eventsObservable: Observable<MviEvent<SimpleLoadingState>>) {
		// No expected events
	}
}