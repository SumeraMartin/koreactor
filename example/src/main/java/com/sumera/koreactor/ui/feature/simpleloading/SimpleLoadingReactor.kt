package com.sumera.koreactor.ui.feature.simpleloading

import com.sumera.koreactor.domain.GetSomeTextDataInteractor
import com.sumera.koreactor.lib.behaviour.ObservableWorker
import com.sumera.koreactor.lib.behaviour.implementation.LoadingBehaviour
import com.sumera.koreactor.lib.behaviour.messages
import com.sumera.koreactor.lib.behaviour.triggers
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.ui.feature.simpleloading.contract.RetryClicked
import com.sumera.koreactor.ui.feature.simpleloading.contract.ShowData
import com.sumera.koreactor.ui.feature.simpleloading.contract.ShowError
import com.sumera.koreactor.ui.feature.simpleloading.contract.ShowLoading
import com.sumera.koreactor.ui.feature.simpleloading.contract.SimpleLoadingState
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import io.reactivex.Observable
import javax.inject.Inject

@PerActivity
class SimpleLoadingReactor @Inject constructor(
		private val dataLoader: GetSomeTextDataInteractor
) : MviReactor<SimpleLoadingState>() {

	override fun createInitialState(): SimpleLoadingState {
		return SimpleLoadingState(isError = false, isLoading = false, data = null)
	}

	override fun bind(actions: Observable<MviAction<SimpleLoadingState>>) {
		val retryClicks = actions.ofActionType<RetryClicked>()

		LoadingBehaviour<Any, String, SimpleLoadingState>(
				triggers = triggers(attachLifecycleObservable, retryClicks),
				loadWorker = ObservableWorker{ dataLoader.execute() },
				cancelPrevious = true,
				loadingMessage = messages({ ShowLoading }),
				errorMessage = messages({ ShowError }),
				dataMessage = messages({ ShowData(it) })
		).bindToView()
	}
}