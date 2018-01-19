package com.sumera.koreactor.ui.feature.simpleloading

import com.sumera.koreactor.domain.GetSomeTextDataInteractor
import com.sumera.koreactor.lib.behaviour.implementation.LoadingBehaviour
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.ui.feature.simpleloading.contract.*
import io.reactivex.Observable

class SimpleLoadingReactor(
		private val dataLoader: GetSomeTextDataInteractor
) : MviReactor<SimpleLoadingState>() {

	override fun createInitialState(): SimpleLoadingState {
		return SimpleLoadingState(isError = false, isLoading = false, data = null)
	}

	override fun bind(actions: Observable<MviAction<SimpleLoadingState>>) {
		val retryClicks = actions.ofActionType<RetryClicked>()

		LoadingBehaviour(
				loadingObservables = listOf(attachLifecycleObservable, retryClicks),
				loadDataAction = { dataLoader.execute() },
				cancelPrevious = true,
				showLoading = { ShowLoading },
				showError = { ShowError },
				showData = { ShowData(it) }
		).bindToView()
	}
}