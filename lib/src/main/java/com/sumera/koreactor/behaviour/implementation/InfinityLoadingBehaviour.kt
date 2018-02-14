package com.sumera.koreactor.behaviour.implementation

import com.sumera.koreactor.behaviour.ErrorMessages
import com.sumera.koreactor.behaviour.InputMessages
import com.sumera.koreactor.behaviour.Messages
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

data class InfinityLoadingBehaviour<INPUT_DATA, OUTPUT_DATA, STATE : MviState>(
		private val initialTriggers: Triggers<out INPUT_DATA>,
		private val loadMoreTriggers: Triggers<out INPUT_DATA>,
		private val loadWorker: SingleWorker<LoadData<INPUT_DATA>, List<OUTPUT_DATA>>,
		private val limit: Int,
		private val initialOffset: Int = 0,
		private val onInitialLoading: InputMessages<INPUT_DATA, STATE>,
		private val onLoadingMore: InputMessages<INPUT_DATA, STATE>,
		private val onInitialError: ErrorMessages<INPUT_DATA, STATE>,
		private val onLoadingMoreError: ErrorMessages<INPUT_DATA, STATE>,
		private val onInitialData: OutputMessages<INPUT_DATA, List<OUTPUT_DATA>, STATE>,
		private val onLoadMoreData: OutputMessages<INPUT_DATA, List<OUTPUT_DATA>, STATE>,
		private val onComplete: Messages<INPUT_DATA, STATE>
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
												listOf(onInitialData.applyData(OutputData(input, data)))
											} else {
												listOf(onLoadMoreData.applyData(OutputData(input, data)))
											}

											if (data.size < limit) {
												isCompleted = true
												values += onComplete.applyData(input)
											}
											offset += data.size

											Observable.fromIterable(values)
										}
										.onErrorReturn { error ->
											if (offset == initialOffset) {
												onInitialError.applyData(ErrorWithData(input, error))
											} else {
												onLoadingMoreError.applyData(ErrorWithData(input, error))
											}
										}
								val startWithData = if (offset == initialOffset) {
									onInitialLoading.applyData(InputData(input))
								} else {
									onLoadingMore.applyData(InputData(input))
								}
								loadingObservable.startWith(startWithData)
							}
				}
	}

	data class LoadData<INPUT_DATA>(
			var input: INPUT_DATA,
			var limit: Int,
			var offset: Int
	)
}
