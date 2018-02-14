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
import com.sumera.koreactor.behaviour.dispatch
import com.sumera.koreactor.behaviour.single
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.Observable

data class SwipeRefreshLoadingBehaviour<INPUT, OUTPUT, STATE : MviState>(
		private val initialTriggers: Triggers<INPUT>,
		private val swipeRefreshTriggers: Triggers<INPUT>,
		private val loadWorker: SingleWorker<INPUT, OUTPUT>,
		private val cancelPrevious: Boolean = true,
		private val onInitialLoading: InputMessages<INPUT, STATE>,
		private val onSwipeRefreshLoading: InputMessages<INPUT, STATE>,
		private val onInitialError: ErrorMessages<INPUT, STATE>,
		private val onSwipeRefreshError: ErrorMessages<INPUT, STATE>,
		private val onInitialData: OutputMessages<INPUT, OUTPUT, STATE>,
		private val onSwipeRefreshData: OutputMessages<INPUT, OUTPUT, STATE>
): MviBehaviour<STATE> {

	override fun createObservable(): Observable<MviReactorMessage<STATE>> {
		val mappedSwipeRefreshTriggers = swipeRefreshTriggers.map { DataWithSwipeRefreshInfo(it, true) }
		val mappedInitialTriggers = swipeRefreshTriggers.map { DataWithSwipeRefreshInfo(it, false) }

		return LoadingBehaviour<DataWithSwipeRefreshInfo<INPUT>, OUTPUT, STATE>(
				triggers = mappedSwipeRefreshTriggers + mappedInitialTriggers,
				loadWorker = single { loadWorker.execute(it.triggerData) },
				cancelPrevious = cancelPrevious,
				onLoading = dispatch { selectCorrectLoadingMessage(it) },
				onError = dispatch { selectCorrectErrorMessage(it) },
				onData = dispatch { selectCorrectDataMessage(it) }
		).createObservable()
	}

	private fun selectCorrectLoadingMessage(inputData: InputData<DataWithSwipeRefreshInfo<INPUT>>) : MviReactorMessage<STATE> {
		val data = InputData(inputData.data.triggerData)
		if (inputData.data.isSwipeRefreshInput) {
			return onSwipeRefreshLoading.applyData(data)
		}
		return onInitialLoading.applyData(data)
	}

	private fun selectCorrectErrorMessage(errorWithData: ErrorWithData<DataWithSwipeRefreshInfo<INPUT>>) : MviReactorMessage<STATE> {
		val data = ErrorWithData(errorWithData.data.triggerData, errorWithData.error)
		if (errorWithData.data.isSwipeRefreshInput) {
			return onSwipeRefreshError.applyData(data)
		}
		return onInitialError.applyData(data)
	}

	private fun selectCorrectDataMessage(inputData: OutputData<DataWithSwipeRefreshInfo<INPUT>, OUTPUT>) : MviReactorMessage<STATE> {
		val data = OutputData(inputData.input.triggerData, inputData.output)
		if (inputData.input.isSwipeRefreshInput) {
			return onSwipeRefreshData.applyData(data)
		}
		return onInitialData.applyData(data)
	}

	data class DataWithSwipeRefreshInfo<out INPUT_DATA>(
			val triggerData: INPUT_DATA,
			val isSwipeRefreshInput: Boolean
	)
}