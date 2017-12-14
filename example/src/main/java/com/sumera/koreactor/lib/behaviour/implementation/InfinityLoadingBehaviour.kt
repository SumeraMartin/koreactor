package com.sumera.koreactor.lib.behaviour.implementation

import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.either.EitherEventOrReducer
import io.reactivex.Observable

data class InfinityLoadingBehaviour<INPUT_DATA, DATA, STATE : MviState, out EVENT_OR_REDUCER : EitherEventOrReducer<STATE>>(
		private val startLoadingObservables: Collection<Observable<out INPUT_DATA>>,
		private val loadDataAction: (INPUT_DATA, Int, Int) -> Observable<List<DATA>>,
		private val limit: Int,
		private val offset: Int,
		private val cancelPrevious: Boolean = false,
		private val showLoadingReducer: (INPUT_DATA) -> EVENT_OR_REDUCER,
		private val showErrorReducer: (Throwable) -> EVENT_OR_REDUCER,
		private val showCompleteReducer: (() -> EVENT_OR_REDUCER)? = null,
		private val showDataReducer: (List<DATA>) -> EVENT_OR_REDUCER
): MviBehaviour<STATE> {

	private var lastOffset = offset

	private var isCompleted = false

	override fun createObservable(): Observable<EitherEventOrReducer<STATE>> {
		if (cancelPrevious) {
			return Observable
					.merge(startLoadingObservables)
					.switchMap { createLoadingObservable(it) }
		}

		return Observable
				.merge(startLoadingObservables)
				.flatMap { createLoadingObservable(it) }
	}

	private fun createLoadingObservable(inputData: INPUT_DATA): Observable<out EVENT_OR_REDUCER> {
		if (isCompleted) {
			return Observable.empty()
		}

		return loadDataAction.invoke(inputData, limit, lastOffset)
				.doOnNext { lastOffset += limit}
				.flatMap { data ->
					val values = listOf(showDataReducer.invoke(data))
					if (data.size < limit) {
						isCompleted = true

						if (showCompleteReducer != null) {
							values.plus(showCompleteReducer.invoke())
						}
					}
					Observable.fromIterable(values)
				}
				.onErrorReturn { showErrorReducer.invoke(it) }
				.startWith(showLoadingReducer.invoke(inputData))
	}
}
