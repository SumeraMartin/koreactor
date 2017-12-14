package com.sumera.koreactor.lib.behaviour.implementation

import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.MviStateReducer
import com.sumera.koreactor.lib.reactor.data.either.EitherEventOrReducer
import io.reactivex.Observable

data class LoadingBehaviour<INPUT_DATA, DATA, STATE : MviState, out REDUCER : MviStateReducer<STATE>>(
		private val loadingObservables: Collection<Observable<out INPUT_DATA>>,
		private val loadDataAction: (INPUT_DATA) -> Observable<out DATA>,
		private val cancelPrevious: Boolean,
		private val showLoadingReducer: (INPUT_DATA) -> EitherEventOrReducer<STATE>,
		private val showErrorReducer: (Throwable) -> EitherEventOrReducer<STATE>,
		private val showDataReducer: (DATA) -> EitherEventOrReducer<STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<EitherEventOrReducer<STATE>> {
		val merged = Observable.merge(loadingObservables)
		if (cancelPrevious) {
			return merged.switchMap { createLoadingObservable(it) }
		}
		return merged.flatMap { createLoadingObservable(it) }
	}

	private fun createLoadingObservable(inputData: INPUT_DATA): Observable<EitherEventOrReducer<STATE>> {
		return loadDataAction.invoke(inputData)
				.map {showDataReducer.invoke(it) }
				.onErrorReturn { showErrorReducer.invoke(it) }
				.startWith(showLoadingReducer.invoke(inputData))
	}
}
