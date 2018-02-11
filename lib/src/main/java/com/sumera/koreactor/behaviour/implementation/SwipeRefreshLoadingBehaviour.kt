package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.Messages
import com.sumera.koreactor.behaviour.MviBehaviour
import com.sumera.koreactor.behaviour.Triggers
import com.sumera.koreactor.behaviour.Worker
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.Observable

data class SwipeRefreshLoadingBehaviour<INPUT_DATA, OUTPUT_DATA, STATE : MviState>(
		private val initialLoadingTriggers: Triggers<out INPUT_DATA>,
		private val swipeRefreshTriggers: Triggers<out INPUT_DATA>,
		private val loadWorker: Worker<INPUT_DATA, OUTPUT_DATA>,
		private val cancelPrevious: Boolean,
		private val loadingMessage: Messages<INPUT_DATA, STATE>,
		private val swipeRefreshMessage: Messages<INPUT_DATA, STATE>,
		private val errorMessage: Messages<Throwable, STATE>,
		private val dataMessage: Messages<OUTPUT_DATA, STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<MviReactorMessage<STATE>> {
		val loadingActions =initialLoadingTriggers.merge()
				.map { DataWithSwipeRefreshInfo(it, false) }

		val swipeRefreshActions = swipeRefreshTriggers.merge()
				.map { DataWithSwipeRefreshInfo(it, true) }

		val merged = Observable.merge(loadingActions, swipeRefreshActions)

		if (cancelPrevious) {
			return merged.switchMap { createLoadingObservable(it) }
		}
		return merged.flatMap { createLoadingObservable(it) }
	}

	private fun createLoadingObservable(dataWithSwipeRefreshInfo: DataWithSwipeRefreshInfo<INPUT_DATA>): Observable<MviReactorMessage<STATE>> {
		val loadMovies = loadWorker.executeAsObservable(dataWithSwipeRefreshInfo.inputData)
				.map { dataMessage.applyData(it) }
				.onErrorReturn { errorMessage.applyData(it) }
		if (dataWithSwipeRefreshInfo.isSwipeRefreshInput) {
			return loadMovies.startWith(swipeRefreshMessage.applyData(dataWithSwipeRefreshInfo.inputData))
		}
		return loadMovies.startWith(loadingMessage.applyData(dataWithSwipeRefreshInfo.inputData))
	}

	data class DataWithSwipeRefreshInfo<out INPUT_DATA>(
			val inputData: INPUT_DATA,
			val isSwipeRefreshInput: Boolean
	)
}