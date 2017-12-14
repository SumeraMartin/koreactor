package com.sumera.koreactor.lib.behaviour.implementation

import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.either.EitherEventOrReducer
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

data class ShowTemporaryBehaviour<INPUT_DATA, STATE : MviState>(
		val startAction: Collection<Observable<INPUT_DATA>>,
		val duration: Long,
		val timeUnit: TimeUnit,
		val cancelPrevious: Boolean,
		val startReducer: (INPUT_DATA) -> EitherEventOrReducer<STATE>,
		val endReducer: () -> EitherEventOrReducer<STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<EitherEventOrReducer<STATE>> {
		val merged = Observable.merge(startAction)
		if (cancelPrevious) {
			return merged.switchMap { createTemporaryObservable(it) }
		}
		return merged.flatMap { createTemporaryObservable(it) }
	}

	private fun createTemporaryObservable(inputData: INPUT_DATA): Observable<EitherEventOrReducer<STATE>> {
		return Observable.just(Unit)
				.delay(duration, timeUnit)
				.map { endReducer.invoke() }
				.startWith(startReducer.invoke(inputData))
	}
}