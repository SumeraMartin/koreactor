package com.sumera.koreactor.lib.behaviour.implementation

import com.sumera.koreactor.lib.behaviour.Messages
import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.behaviour.Triggers
import com.sumera.koreactor.lib.behaviour.Worker
import com.sumera.koreactor.lib.reactor.data.MviReactorMessage
import com.sumera.koreactor.lib.reactor.data.MviState
import io.reactivex.Observable

data class LoadingBehaviour<INPUT_DATA, OUTPUT_DATA, STATE : MviState>(
		private val triggers: Triggers<out INPUT_DATA>,
		private val loadWorker: Worker<INPUT_DATA, out OUTPUT_DATA>,
		private val cancelPrevious: Boolean,
		private val loadingMessage: Messages<INPUT_DATA, STATE>,
		private val errorMessage: Messages<Throwable, STATE>,
		private val dataMessage: Messages<OUTPUT_DATA, STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<MviReactorMessage<STATE>> {
		val merged = Observable.merge(triggers.observables)
		if (cancelPrevious) {
			return merged.switchMap { createLoadingObservable(it) }
		}
		return merged.flatMap { createLoadingObservable(it) }
	}

	private fun createLoadingObservable(inputData: INPUT_DATA): Observable<MviReactorMessage<STATE>> {
		return loadWorker.execute(inputData)
				.map { dataMessage.applyData(it) }
				.onErrorReturn { errorMessage.applyData(it) }
				.startWith(loadingMessage.applyData(inputData))
	}
}