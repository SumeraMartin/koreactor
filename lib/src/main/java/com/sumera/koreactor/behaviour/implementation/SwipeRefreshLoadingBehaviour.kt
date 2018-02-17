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

data class SwipeRefreshLoadingBehaviour<INPUT, OUTPUT, STATE : MviState>(
		private val initialTriggers: Triggers<INPUT>,
		private val swipeRefreshTriggers: Triggers<INPUT>,
		private val loadWorker: SingleWorker<InputData<INPUT>, OUTPUT>,
		private val onInitialLoading: InputMessages<INPUT, STATE>,
		private val onSwipeRefreshLoading: InputMessages<INPUT, STATE>,
		private val onInitialError: ErrorMessages<INPUT, STATE>,
		private val onSwipeRefreshError: ErrorMessages<INPUT, STATE>,
		private val onInitialData: OutputMessages<INPUT, OUTPUT, STATE>,
		private val onSwipeRefreshData: OutputMessages<INPUT, OUTPUT, STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<MviReactorMessage<STATE>> {
		val mappedSwipeRefreshTriggers = swipeRefreshTriggers.map { DataWithSwipeRefreshInfo(it, true) }
		val mappedInitialTriggers = initialTriggers.map { DataWithSwipeRefreshInfo(it, false) }
		val mappedTriggers = mappedSwipeRefreshTriggers + mappedInitialTriggers

		return mappedTriggers.merge().switchMap { createLoadingObservable(it) }
	}

	private fun createLoadingObservable(input: DataWithSwipeRefreshInfo<INPUT>): Observable<MviReactorMessage<STATE>> {
		return loadWorker.executeAsObservable(InputData(input.triggerData))
				.map { selectCorrectDataMessage(input, it) }
				.onErrorReturn { selectCorrectErrorMessage(it, input) }
				.startWith(selectCorrectLoadingMessage(input))
	}

	private fun selectCorrectLoadingMessage(dataWithSwipeRefreshInfo: DataWithSwipeRefreshInfo<INPUT>) : MviReactorMessage<STATE> {
		val data = InputData(dataWithSwipeRefreshInfo.triggerData)
		if (dataWithSwipeRefreshInfo.isSwipeRefreshInput) {
			return onSwipeRefreshLoading.applyData(data)
		}
		return onInitialLoading.applyData(data)
	}

	private fun selectCorrectErrorMessage(error: Throwable, dataWithSwipeRefreshInfo: DataWithSwipeRefreshInfo<INPUT>) : MviReactorMessage<STATE> {
		val errorWithData = ErrorWithData(dataWithSwipeRefreshInfo.triggerData, error)
		if (dataWithSwipeRefreshInfo.isSwipeRefreshInput) {
			return onSwipeRefreshError.applyData(errorWithData)
		}
		return onInitialError.applyData(errorWithData)
	}

	private fun selectCorrectDataMessage(dataWithSwipeRefreshInfo: DataWithSwipeRefreshInfo<INPUT>, output: OUTPUT) : MviReactorMessage<STATE> {
		val data = OutputData(dataWithSwipeRefreshInfo.triggerData, output)
		if (dataWithSwipeRefreshInfo.isSwipeRefreshInput) {
			return onSwipeRefreshData.applyData(data)
		}
		return onInitialData.applyData(data)
	}

	data class DataWithSwipeRefreshInfo<out INPUT_DATA>(
			val triggerData: INPUT_DATA,
			val isSwipeRefreshInput: Boolean
	)
}