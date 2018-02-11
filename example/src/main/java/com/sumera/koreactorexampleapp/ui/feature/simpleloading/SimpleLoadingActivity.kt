package com.sumera.koreactorexampleapp.ui.feature.simpleloading

import android.os.Bundle
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.util.data.asOptional
import com.sumera.koreactor.util.extension.getFalse
import com.sumera.koreactor.util.extension.getNotNull
import com.sumera.koreactor.util.extension.getTrue
import com.sumera.koreactorexampleapp.R
import com.sumera.koreactorexampleapp.ui.base.BaseActivity
import com.sumera.koreactorexampleapp.ui.common.PlaceholderLayout
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.contract.RetryClicked
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.contract.SimpleLoadingState
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_simple_loading.*
import javax.inject.Inject

class SimpleLoadingActivity : BaseActivity<SimpleLoadingState>() {

	@Inject lateinit var reactorFactory: SimpleLoadingReactorFactory

	override val layoutRes: Int
		get() = R.layout.activity_simple_loading

	override fun createReactor(): MviReactor<SimpleLoadingState> {
		return getReactor(reactorFactory, SimpleLoadingReactor::class.java)
	}

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
}