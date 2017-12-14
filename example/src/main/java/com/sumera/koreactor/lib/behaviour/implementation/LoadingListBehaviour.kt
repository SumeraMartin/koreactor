package com.sumera.koreactor.lib.behaviour.implementation

import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.either.EitherEventOrReducer
import io.reactivex.Observable

data class LoadingListBehaviour<INPUT_DATA, DATA, STATE : MviState>(
		private val loadingObservables: Collection<Observable<out INPUT_DATA>>,
		private val loadDataAction: (INPUT_DATA) -> Observable<out List<DATA>>,
		private val cancelPrevious: Boolean,
		private val showLoadingReducer: (INPUT_DATA) -> EitherEventOrReducer<STATE>,
		private val showErrorReducer: (Throwable) -> EitherEventOrReducer<STATE>,
		private val showEmptyReducer: (() -> EitherEventOrReducer<STATE>)? = null,
		private val showDataReducer: (List<DATA>) -> EitherEventOrReducer<STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<EitherEventOrReducer<STATE>> {
		if (cancelPrevious) {
			return Observable
					.merge(loadingObservables)
					.switchMap { createLoadingObservable(it) }
		}

		return Observable
				.merge(loadingObservables)
				.flatMap { createLoadingObservable(it) }
	}

	private fun createLoadingObservable(inputData: INPUT_DATA): Observable<EitherEventOrReducer<STATE>> {
		return loadDataAction.invoke(inputData)
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
				.startWith(showLoadingReducer.invoke(inputData))
	}
}
