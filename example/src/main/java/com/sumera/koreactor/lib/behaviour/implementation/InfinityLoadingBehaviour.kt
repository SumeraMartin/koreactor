package com.sumera.koreactor.lib.behaviour.implementation

import com.sumera.koreactor.lib.behaviour.Messages
import com.sumera.koreactor.lib.behaviour.MviBehaviour
import com.sumera.koreactor.lib.behaviour.Triggers
import com.sumera.koreactor.lib.behaviour.Worker
import com.sumera.koreactor.lib.reactor.data.MviReactorMessage
import com.sumera.koreactor.lib.reactor.data.MviState
import io.reactivex.Observable

data class InfinityLoadingBehaviour<INPUT_DATA, OUTPUT_DATA, STATE : MviState>(
		private val triggers: Triggers<out INPUT_DATA>,
		private val loadWorker: Worker<LoadData<INPUT_DATA>, List<OUTPUT_DATA>>,
		private val limit: Int,
		private val initialOffset: Int,
		private val cancelPrevious: Boolean = false,
		private val loadingMessage: Messages<INPUT_DATA, STATE>,
		private val errorMessage: Messages<Throwable, STATE>,
		private val completeMessage: Messages<INPUT_DATA, STATE>,
		private val dataMessage: Messages<List<OUTPUT_DATA>, STATE>
): MviBehaviour<STATE> {

	private var lastOffset = initialOffset

	private var isCompleted = false

	override fun createObservable(): Observable<MviReactorMessage<STATE>> {
		if (cancelPrevious) {
			return Observable
					.merge(triggers.observables)
					.switchMap { createLoadingObservable(it) }
		}

		return Observable
				.merge(triggers.observables)
				.flatMap { createLoadingObservable(it) }
	}

	private fun createLoadingObservable(inputData: INPUT_DATA): Observable<MviReactorMessage<STATE>> {
		if (isCompleted) {
			return Observable.empty()
		}

		return loadWorker.execute(LoadData(inputData, limit, lastOffset))
				.doOnNext { lastOffset += limit}
				.flatMap { data ->
					val values = listOf(dataMessage.applyData(data))
					if (data.size < limit) {
						isCompleted = true
						values.plus(completeMessage.applyData(inputData))
					}
					Observable.fromIterable(values)
				}
				.onErrorReturn { error -> errorMessage.applyData(error) }
				.startWith(loadingMessage.applyData(inputData))
	}

    data class LoadData<INPUT_DATA>(
            var input: INPUT_DATA,
            var limit: Int,
            var offset: Int
    )
}
