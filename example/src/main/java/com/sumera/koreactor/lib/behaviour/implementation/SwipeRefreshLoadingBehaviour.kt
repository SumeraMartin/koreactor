package com.sumera.koreactor.lib.behaviour.implementation

import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.either.EitherEventOrReducer
import io.reactivex.Observable

data class SwipeRefreshLoadingBehaviour<INPUT_DATA, DATA, STATE : MviState>(
		private val loadingObservables: Collection<Observable<out INPUT_DATA>>,
		private val swipeRefreshObservables: Collection<Observable<out INPUT_DATA>>,
		private val loadDataAction: (INPUT_DATA) -> Observable<DATA>,
		private val cancelPrevious: Boolean,
		private val showLoadingReducer: (INPUT_DATA) -> EitherEventOrReducer<STATE>,
		private val showSwipeRefreshReducer: () -> EitherEventOrReducer<STATE>,
		private val showErrorReducer: (Throwable) -> EitherEventOrReducer<STATE>,
		private val showDataReducer: (DATA) -> EitherEventOrReducer<STATE>
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
				.map { showDataReducer.invoke(it) }
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