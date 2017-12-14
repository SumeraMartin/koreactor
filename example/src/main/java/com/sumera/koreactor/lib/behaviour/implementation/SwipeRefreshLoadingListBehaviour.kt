package com.sumera.koreactor.lib.behaviour.implementation

import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.either.EitherEventOrReducer
import io.reactivex.Observable

data class SwipeRefreshLoadingListBehaviour<INPUT_DATA, DATA, STATE : MviState>(
		private val loadingObservables: Collection<Observable<out INPUT_DATA>>,
		private val swipeRefreshObservables: Collection<Observable<out INPUT_DATA>>,
		private val loadDataAction: (INPUT_DATA) -> Observable<List<DATA>>,
		private val cancelPrevious: Boolean,
		private val showLoadingReducer: (INPUT_DATA) -> EitherEventOrReducer<STATE>,
		private val showSwipeRefreshReducer: () -> EitherEventOrReducer<STATE>,
		private val showErrorReducer: (Throwable) -> EitherEventOrReducer<STATE>,
		private val showEmptyReducer: (() -> EitherEventOrReducer<STATE>)? = null,
		private val showDataReducer: (List<DATA>) -> EitherEventOrReducer<STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<EitherEventOrReducer<STATE>> {
		val loadingActions = Observable
				.merge(loadingObservables)
				.map { DataWithSwipeRefreshInfo(it, false) }

		val swipeRefreshActions = Observable
				.merge(swipeRefreshObservables)
				.map { DataWithSwipeRefreshInfo(it, true) }

		val merged = Observable.merge(loadingActions, swipeRefreshActions)

		if (cancelPrevious) {
			return merged.switchMap { createLoadingObservable(it) }
		}
		return merged.flatMap { createLoadingObservable(it) }
	}

	private fun createLoadingObservable(dataWithSwipeRefreshInfo: DataWithSwipeRefreshInfo<INPUT_DATA>): Observable<EitherEventOrReducer<STATE>> {
		val loadMovies = loadDataAction.invoke(dataWithSwipeRefreshInfo.inputData)
				.map {
					if (showEmptyReducer == null) {
						showDataReducer.invoke(it)
					} else {
						if (it.isNotEmpty()) {
							showDataReducer.invoke(it)
						} else {
							showEmptyReducer.invoke()
						}
					}

				}
				.onErrorReturn { showErrorReducer.invoke(it) }
		if (dataWithSwipeRefreshInfo.isSwipeRefreshInput) {
			return loadMovies.startWith(showSwipeRefreshReducer.invoke())
		}
		return loadMovies.startWith(showLoadingReducer.invoke(dataWithSwipeRefreshInfo.inputData))
	}

	data class DataWithSwipeRefreshInfo<INPUT_DATA>(
			val inputData: INPUT_DATA,
			val isSwipeRefreshInput: Boolean
	)
}