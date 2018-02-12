package com.sumera.koreactorexampleapp.ui.feature.infinity

import com.sumera.koreactor.behaviour.implementation.InfinityLoadingBehaviour
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.single
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.AttachState
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactorexampleapp.data.ToDoItem
import com.sumera.koreactorexampleapp.injection.PerActivity
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.AppendNewData
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.InfinityState
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.NavigateToDetailEvent
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.OnItemClickedAction
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.OnRetryInfinityLoadingAction
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.OnRetryInitialAction
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.OnScrolledToBottomAction
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.SetNewData
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.ShowCompleted
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.ShowInfinityError
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.ShowInfinityLoading
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.ShowInitialError
import com.sumera.koreactorexampleapp.ui.feature.infinity.contract.ShowInitialLoading
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerActivity
class InfinityReactor @Inject constructor(

) : MviReactor<InfinityState>() {

	override fun createInitialState(): InfinityState {
		return InfinityState(
				isInitialLoading = true,
				isInitialError = false,
				isInfinityLoading = false,
				isInfinityError = false,
				isCompleted = false,
				data = null)
	}

	override fun bind(actions: Observable<MviAction<InfinityState>>) {
		val attachAction = lifecycleObservable.ofLifecycleType<AttachState>()
		val retryInitialAction = actions.ofActionType<OnRetryInitialAction>()
		val bottomScrolledAction = actions.ofActionType<OnScrolledToBottomAction>()
		val retryInfinityLoadingAction = actions.ofActionType<OnRetryInfinityLoadingAction>()
		val itemClickedAction = actions.ofActionType<OnItemClickedAction>()

		val startLoadingNextDataAction = bottomScrolledAction
				.withLatestFrom(stateObservable, BiFunction { _: OnScrolledToBottomAction, state: InfinityState -> state })
				.filter { !it.isInfinityLoading && !it.isInfinityError && !it.isCompleted }

		InfinityLoadingBehaviour<Any, ToDoItem, InfinityState>(
				initialTriggers = triggers(attachAction, retryInitialAction),
				loadMoreTriggers = triggers(startLoadingNextDataAction, retryInfinityLoadingAction),
				loadWorker = single { input -> returnsSomeData(input.limit, input.offset) },
				limit = 10,
				initialOffset = 0,
				initialLoadingMessage = messages { ShowInitialLoading },
				loadingMoreMessage = messages { ShowInfinityLoading },
				initialErrorMessage = messages { ShowInitialError },
				loadingMoreErrorMessage = messages { ShowInfinityError },
				completeMessage = messages { ShowCompleted },
				initialDataMessage = messages { SetNewData(it) },
				loadMoreDataMessage = messages { AppendNewData(it) }
		).bindToView()

		itemClickedAction
				.map { NavigateToDetailEvent }
				.bindToView()
	}

	private fun returnsSomeData(limit: Int, offset: Int): Single<List<ToDoItem>> {
		return Single.fromCallable {
			var list = listOf<ToDoItem>()
			if (offset > 30) {
				for (i in offset..(offset + 3)) {
					list = list.plus(ToDoItem(id = i, title = "Title $i"))
				}
			} else {
				for (i in offset until (offset + limit)) {
					list = list.plus(ToDoItem(id = i, title = "Title $i"))
				}
			}
			return@fromCallable list
		}
				.delay(1, TimeUnit.SECONDS)
				.flatMap {
					if (Random().nextInt(3) % 3 == 0) {
						return@flatMap Single.error<List<ToDoItem>>(IllegalStateException("Error"))
					}
					return@flatMap Single.just(it)
				}
	}

}