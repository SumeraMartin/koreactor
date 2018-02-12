package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.Messages
import com.sumera.koreactor.behaviour.MviBehaviour
import com.sumera.koreactor.behaviour.SingleWorker
import com.sumera.koreactor.behaviour.Triggers
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.Observable

data class InfinityLoadingBehaviour<INPUT_DATA, OUTPUT_DATA, STATE : MviState>(
		private val initialTriggers: Triggers<out INPUT_DATA>,
		private val loadMoreTriggers: Triggers<out INPUT_DATA>,
		private val loadWorker: SingleWorker<LoadData<INPUT_DATA>, List<OUTPUT_DATA>>,
		private val limit: Int,
		private val initialOffset: Int,
		private val initialLoadingMessage: Messages<INPUT_DATA, STATE>,
		private val loadingMoreMessage: Messages<INPUT_DATA, STATE>,
		private val initialErrorMessage: Messages<Throwable, STATE>,
		private val loadingMoreErrorMessage: Messages<Throwable, STATE>,
		private val completeMessage: Messages<INPUT_DATA, STATE>,
		private val initialDataMessage: Messages<List<OUTPUT_DATA>, STATE>,
		private val loadMoreDataMessage: Messages<List<OUTPUT_DATA>, STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<MviReactorMessage<STATE>> {
		return initialTriggers.merge()
				.switchMap { inputData ->
					var offset = this.initialOffset
					var isCompleted = false
					Observable.merge(Observable.just(inputData), loadMoreTriggers.merge())
							.filter { !isCompleted }
							.concatMap { input ->
								val loadingObservable = loadWorker.executeAsObservable(LoadData(input, limit, offset))
										.flatMap { data ->
											var values = if (offset == initialOffset) {
												listOf(initialDataMessage.applyData(data))
											} else {
												listOf(loadMoreDataMessage.applyData(data))
											}

											if (data.size < limit) {
												isCompleted = true
												values += completeMessage.applyData(input)
											}
											offset += data.size

											Observable.fromIterable(values)
										}
										.onErrorReturn { error ->
											if (offset == initialOffset) {
												initialErrorMessage.applyData(error)
											} else {
												loadingMoreErrorMessage.applyData(error)
											}
										}
								val startWithData = if (offset == initialOffset) {
									initialLoadingMessage.applyData(input)
								} else {
									loadingMoreMessage.applyData(input)
								}
								loadingObservable
										.startWith(startWithData)
							}
				}
	}

	data class LoadData<INPUT_DATA>(
			var input: INPUT_DATA,
			var limit: Int,
			var offset: Int
	)
}
