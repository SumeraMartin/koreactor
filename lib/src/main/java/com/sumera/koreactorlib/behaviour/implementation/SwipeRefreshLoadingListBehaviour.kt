package com.sumera.koreactorlib.behaviour.implementation

import com.sumera.koreactorlib.behaviour.Messages
import com.sumera.koreactorlib.behaviour.MviBehaviour
import com.sumera.koreactorlib.behaviour.Triggers
import com.sumera.koreactorlib.behaviour.Worker
import com.sumera.koreactorlib.reactor.data.MviReactorMessage
import com.sumera.koreactorlib.reactor.data.MviState
import io.reactivex.Observable

data class SwipeRefreshLoadingListBehaviour<INPUT_DATA, DATA, STATE : MviState>(
		private val initialLoadingTriggers: Triggers<out INPUT_DATA>,
		private val swipeRefreshTriggers: Triggers<out INPUT_DATA>,
		private val loadWorker: Worker<INPUT_DATA, List<DATA>>,
		private val cancelPrevious: Boolean,
		private val loadingMessage: Messages<INPUT_DATA, STATE>,
		private val swipeRefreshMessage: Messages<INPUT_DATA, STATE>,
		private val errorMessage: Messages<Throwable, STATE>,
		private val emptyMessage: Messages<INPUT_DATA, STATE>,
		private val dataMessage: Messages<List<DATA>, STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<MviReactorMessage<STATE>> {
		val loadingActions = Observable
				.merge(initialLoadingTriggers.observables)
				.map { DataWithSwipeRefreshInfo(it, false) }

		val swipeRefreshActions = Observable
				.merge(swipeRefreshTriggers.observables)
				.map { DataWithSwipeRefreshInfo(it, true) }

		val merged = Observable.merge(loadingActions, swipeRefreshActions)

		if (cancelPrevious) {
			return merged.switchMap { createLoadingObservable(it) }
		}
		return merged.flatMap { createLoadingObservable(it) }
	}

	private fun createLoadingObservable(dataWithSwipeRefreshInfo: DataWithSwipeRefreshInfo<INPUT_DATA>): Observable<MviReactorMessage<STATE>> {
		val loadMovies = loadWorker.execute(dataWithSwipeRefreshInfo.inputData)
				.map {
					if (it.isNotEmpty()) {
						return@map dataMessage.applyData(it)
					} else {
						return@map emptyMessage.applyData(dataWithSwipeRefreshInfo.inputData)
					}
				}
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