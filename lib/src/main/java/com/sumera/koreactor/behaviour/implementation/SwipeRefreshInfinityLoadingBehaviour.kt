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

data class SwipeRefreshInfinityLoadingBehaviour<INPUT, OUTPUT, STATE : MviState>(
        private val initialTriggers: Triggers<out INPUT>,
        private val swipeRefreshTriggers: Triggers<out INPUT>,
        private val loadMoreTriggers: Triggers<out INPUT>,
        private val loadWorker: SingleWorker<LoadData<INPUT>, List<OUTPUT>>,
        private val limit: Int,
        private val initialOffset: Int = 0,
        private val onInitialLoading: InputMessages<INPUT, STATE>,
        private val onSwipeRefreshLoading: InputMessages<INPUT, STATE>,
        private val onLoadingMore: InputMessages<INPUT, STATE>,
        private val onInitialError: ErrorMessages<INPUT, STATE>,
        private val onSwipeRefreshError: ErrorMessages<INPUT, STATE>,
        private val onLoadingMoreError: ErrorMessages<INPUT, STATE>,
        private val onInitialData: OutputMessages<INPUT, List<OUTPUT>, STATE>,
        private val onSwipeRefreshData: OutputMessages<INPUT, List<OUTPUT>, STATE>,
        private val onLoadMoreData: OutputMessages<INPUT, List<OUTPUT>, STATE>,
        private val onComplete: InputMessages<INPUT, STATE>
): MviBehaviour<STATE> {

    override fun createObservable(): Observable<MviReactorMessage<STATE>> {
        val wrappedInitialTriggers = initialTriggers.map { DataWithTriggerInfo(it, TriggerSourceType.SWIPE_REFRESH) }
        val wrappedSwipeRefreshTriggers = swipeRefreshTriggers.map { DataWithTriggerInfo(it, TriggerSourceType.SWIPE_REFRESH) }
        val wrappedLoadMoreTriggers = loadMoreTriggers.map { DataWithTriggerInfo(it, TriggerSourceType.LOAD_MORE) }

        return InfinityLoadingBehaviour<DataWithTriggerInfo<INPUT>, OUTPUT, STATE>(
                initialTriggers = wrappedInitialTriggers + wrappedSwipeRefreshTriggers,
                loadMoreTriggers = wrappedLoadMoreTriggers,
                loadWorker = single { loadWorker.execute(transformInput(it)) },
                limit = limit,
                initialOffset = initialOffset,
                onInitialLoading = dispatch { selectCorrectLoadingMessage(it) },
                onLoadingMore = dispatch { selectCorrectLoadingMessage(it) },
                onInitialError = dispatch { selectCorrectErrorMessage(it) },
                onLoadingMoreError = dispatch { selectCorrectErrorMessage(it) },
                onInitialData = dispatch { selectCorrectDataMessage(it) },
                onLoadMoreData = dispatch { selectCorrectDataMessage(it) },
                onComplete = dispatch { onComplete.applyData(InputData(it.triggerInput)) }
        ).createObservable()
    }

    private fun selectCorrectLoadingMessage(inputData: InputData<DataWithTriggerInfo<INPUT>>) : MviReactorMessage<STATE> {
        val data = InputData(inputData.data.triggerInput)
        return when(inputData.data.triggerSourceType) {
            TriggerSourceType.INITIAL -> onInitialLoading.applyData(data)
            TriggerSourceType.SWIPE_REFRESH -> onSwipeRefreshLoading.applyData(data)
            TriggerSourceType.LOAD_MORE -> onLoadingMore.applyData(data)
        }
    }

    private fun selectCorrectErrorMessage(errorWithData: ErrorWithData<DataWithTriggerInfo<INPUT>>) : MviReactorMessage<STATE> {
        val data = ErrorWithData(errorWithData.data.triggerInput, errorWithData.error)
        return when(errorWithData.data.triggerSourceType) {
            TriggerSourceType.INITIAL -> onInitialError.applyData(data)
            TriggerSourceType.SWIPE_REFRESH -> onSwipeRefreshError.applyData(data)
            TriggerSourceType.LOAD_MORE -> onLoadingMoreError.applyData(data)
        }
    }

    private fun selectCorrectDataMessage(outputData: OutputData<DataWithTriggerInfo<INPUT>, List<OUTPUT>>) : MviReactorMessage<STATE> {
        val data = OutputData(outputData.input.triggerInput, outputData.output)
        return when(outputData.input.triggerSourceType) {
            TriggerSourceType.INITIAL -> onInitialData.applyData(data)
            TriggerSourceType.SWIPE_REFRESH -> onSwipeRefreshData.applyData(data)
            TriggerSourceType.LOAD_MORE -> onLoadMoreData.applyData(data)
        }
    }

    private fun transformInput(loadData: InfinityLoadingBehaviour.LoadData<DataWithTriggerInfo<INPUT>>): LoadData<INPUT> {
        return LoadData(loadData.input.triggerInput, loadData.limit, loadData.offset)
    }

    data class LoadData<INPUT_DATA>(
            var input: INPUT_DATA,
            var limit: Int,
            var offset: Int
    )

    data class DataWithTriggerInfo<out INPUT_DATA>(
            val triggerInput: INPUT_DATA,
            val triggerSourceType: TriggerSourceType
    )

    enum class TriggerSourceType {
        INITIAL, SWIPE_REFRESH, LOAD_MORE
    }
}