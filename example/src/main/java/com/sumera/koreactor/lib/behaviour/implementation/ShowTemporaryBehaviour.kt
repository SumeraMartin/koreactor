package com.sumera.koreactor.lib.behaviour.implementation

import com.sumera.koreactor.lib.behaviour.Messages
import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.behaviour.Triggers
import com.sumera.koreactor.lib.reactor.data.MviReactorMessage
import com.sumera.koreactor.lib.reactor.data.MviState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

data class ShowTemporaryBehaviour<INPUT_DATA, STATE : MviState>(
        private val triggers: Triggers<INPUT_DATA>,
		private val duration: Long,
		private val timeUnit: TimeUnit,
		private val cancelPrevious: Boolean,
		private val startMessage: Messages<INPUT_DATA, STATE>,
		private val endMessage: Messages<INPUT_DATA, STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<MviReactorMessage<STATE>> {
		val merged = Observable.merge(triggers.observables)
		if (cancelPrevious) {
			return merged.switchMap { createTemporaryObservable(it) }
		}
		return merged.flatMap { createTemporaryObservable(it) }
	}

	private fun createTemporaryObservable(inputData: INPUT_DATA): Observable<MviReactorMessage<STATE>> {
		return Observable.just(Unit)
				.delay(duration, timeUnit)
				.map { startMessage.applyData(inputData) }
				.startWith(endMessage.applyData(inputData))
	}
}