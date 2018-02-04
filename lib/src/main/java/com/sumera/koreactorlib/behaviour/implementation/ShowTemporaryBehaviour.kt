package com.sumera.koreactorlib.behaviour.implementation

import com.sumera.koreactorlib.behaviour.Messages
import com.sumera.koreactorlib.behaviour.MviBehaviour
import com.sumera.koreactorlib.behaviour.Triggers
import com.sumera.koreactorlib.reactor.data.MviReactorMessage
import com.sumera.koreactorlib.reactor.data.MviState
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
				.map { endMessage.applyData(inputData) }
				.startWith(startMessage.applyData(inputData))
	}
}