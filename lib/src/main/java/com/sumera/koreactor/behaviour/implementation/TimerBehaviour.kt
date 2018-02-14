package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.Messages
import com.sumera.koreactor.behaviour.MviBehaviour
import com.sumera.koreactor.behaviour.Triggers
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

data class TimerBehaviour<INPUT_DATA, STATE : MviState>(
        private val triggers: Triggers<out INPUT_DATA>,
        private val duration: Long,
        private val timeUnit: TimeUnit,
        private val cancelPrevious: Boolean,
        private val onStart: Messages<INPUT_DATA, STATE>,
        private val onEnd: Messages<INPUT_DATA, STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<MviReactorMessage<STATE>> {
		val merged = triggers.merge()
		if (cancelPrevious) {
			return merged.switchMap { createTemporaryObservable(it) }
		}
		return merged.flatMap { createTemporaryObservable(it) }
	}

	private fun createTemporaryObservable(inputData: INPUT_DATA): Observable<MviReactorMessage<STATE>> {
		return Observable.just(Unit)
				.delay(duration, timeUnit)
				.map { onEnd.applyData(inputData) }
				.startWith(onStart.applyData(inputData))
	}
}