package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.Messages
import com.sumera.koreactor.behaviour.MviBehaviour
import com.sumera.koreactor.behaviour.SingleWorker
import com.sumera.koreactor.behaviour.Triggers
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.Observable

data class LoadingBehaviour<INPUT_DATA, OUTPUT_DATA, STATE : MviState>(
		private val triggers: Triggers<out INPUT_DATA>,
		private val loadWorker: SingleWorker<INPUT_DATA, out OUTPUT_DATA>,
		private val cancelPrevious: Boolean,
		private val loadingMessage: Messages<INPUT_DATA, STATE>,
		private val errorMessage: Messages<Throwable, STATE>,
		private val dataMessage: Messages<OUTPUT_DATA, STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<MviReactorMessage<STATE>> {
		if (cancelPrevious) {
			return triggers.merge().switchMap { createLoadingObservable(it) }
		}
		return triggers.merge().flatMap { createLoadingObservable(it) }
	}

	private fun createLoadingObservable(inputData: INPUT_DATA): Observable<MviReactorMessage<STATE>> {
		return loadWorker.executeAsObservable(inputData)
				.map { dataMessage.applyData(it) }
				.onErrorReturn { errorMessage.applyData(it) }
				.startWith(loadingMessage.applyData(inputData))
	}
}