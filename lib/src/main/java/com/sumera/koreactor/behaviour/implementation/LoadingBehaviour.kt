package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.ErrorMessages
import com.sumera.koreactor.behaviour.InputMessages
import com.sumera.koreactor.behaviour.MviBehaviour
import com.sumera.koreactor.behaviour.OutputMessages
import com.sumera.koreactor.behaviour.SingleWorker
import com.sumera.koreactor.behaviour.Triggers
import com.sumera.koreactor.behaviour.data.ErrorWithData
import com.sumera.koreactor.behaviour.data.InputData
import com.sumera.koreactor.behaviour.data.OutputData
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.Observable

data class LoadingBehaviour<INPUT, OUTPUT, STATE : MviState>(
		private val triggers: Triggers<out INPUT>,
		private val loadWorker: SingleWorker<INPUT, OUTPUT>,
		private val cancelPrevious: Boolean,
		private val onLoading: InputMessages<INPUT, STATE>,
		private val onError: ErrorMessages<INPUT, STATE>,
		private val onData: OutputMessages<INPUT, OUTPUT, STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<MviReactorMessage<STATE>> {
		if (cancelPrevious) {
			return triggers.merge().switchMap { createLoadingObservable(it) }
		}
		return triggers.merge().flatMap { createLoadingObservable(it) }
	}

	private fun createLoadingObservable(input: INPUT): Observable<MviReactorMessage<STATE>> {
		return loadWorker.executeAsObservable(input)
				.map { onData.applyData(OutputData(input, it)) }
				.onErrorReturn { onError.applyData(ErrorWithData(input, it)) }
				.startWith(onLoading.applyData(InputData(input)))
	}
}