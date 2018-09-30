package com.sumera.koreactorexampleapp.ui.feature.simpleloading

import com.sumera.koreactor.behaviour.implementation.LoadingBehaviour
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.single
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.util.bundle.BundleWrapper
import com.sumera.koreactorexampleapp.domain.GetSomeTextDataInteractor
import com.sumera.koreactorexampleapp.injection.PerActivity
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.contract.RetryClicked
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.contract.ShowData
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.contract.ShowError
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.contract.ShowLoading
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.contract.SimpleLoadingState
import io.reactivex.Observable
import javax.inject.Inject

@PerActivity
class SimpleLoadingReactor @Inject constructor(
		private val dataLoader: GetSomeTextDataInteractor
) : MviReactor<SimpleLoadingState>() {

	companion object {
	    private const val DATA_KEY = "data_key"
	}

	override fun createInitialState(): SimpleLoadingState {
		return SimpleLoadingState(isError = false, isLoading = false, data = null)
	}

	override fun onSaveInstanceState(state: SimpleLoadingState, bundleWrapper: BundleWrapper) {
		bundleWrapper.putString(DATA_KEY, state.data)
	}

	override fun onRestoreSaveInstanceState(state: SimpleLoadingState, bundleWrapper: BundleWrapper): SimpleLoadingState {
		return state.copy(data = bundleWrapper.getString(DATA_KEY))
	}

	override fun bind(actions: Observable<MviAction<SimpleLoadingState>>) {
		val retryClicks = actions.ofActionType<RetryClicked>()

		val loadOnAttachIfNotPreloaded = attachLifecycleObservable.switchMapSingle { stateSingle }
				.filter { it.data == null }

		LoadingBehaviour<Any, String, SimpleLoadingState>(
				triggers = triggers(loadOnAttachIfNotPreloaded, retryClicks),
				loadWorker = single { dataLoader.execute() },
				cancelPrevious = true,
				loadingMessage = messages { ShowLoading },
				errorMessage = messages { ShowError },
				dataMessage = messages { ShowData(it) }
		).bindToView()
	}
}