package com.sumera.koreactor.ui.feature.todo

import com.sumera.koreactor.data.ToDoItem
import com.sumera.koreactor.lib.behaviour.ObservableWorker
import com.sumera.koreactor.lib.behaviour.implementation.InfinityLoadingBehaviour
import com.sumera.koreactor.lib.behaviour.implementation.LoadingListBehaviour
import com.sumera.koreactor.lib.behaviour.messages
import com.sumera.koreactor.lib.behaviour.triggers
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.lib.reactor.lifecycle.AttachState
import com.sumera.koreactor.lib.util.extension.ofLifecycleType
import com.sumera.koreactor.ui.feature.infinity.contract.AddNewData
import com.sumera.koreactor.ui.feature.infinity.contract.NavigateToDetailEvent
import com.sumera.koreactor.ui.feature.infinity.contract.OnItemClickedAction
import com.sumera.koreactor.ui.feature.infinity.contract.OnRetryInfinityLoadingAction
import com.sumera.koreactor.ui.feature.infinity.contract.OnRetryInitialAction
import com.sumera.koreactor.ui.feature.infinity.contract.OnScrolledToBottomAction
import com.sumera.koreactor.ui.feature.infinity.contract.ShowInfinityError
import com.sumera.koreactor.ui.feature.infinity.contract.ShowInfinityLoading
import com.sumera.koreactor.ui.feature.infinity.contract.ShowInitialError
import com.sumera.koreactor.ui.feature.infinity.contract.ShowInitialLoading
import com.sumera.koreactor.ui.feature.todo.contract.InfinityState
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import io.reactivex.Observable
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
				data = null)
	}

	override fun bind(actions: Observable<MviAction<InfinityState>>) {
		val attachAction = lifecycleObservable.ofLifecycleType<AttachState>()
		val retryInitialAction = actions.ofActionType<OnRetryInitialAction>()
		val bottomScrolledAction = actions.ofActionType<OnScrolledToBottomAction>()
		val retryInfinityLoadingAction = actions.ofActionType<OnRetryInfinityLoadingAction>()
		val itemClickedAction = actions.ofActionType<OnItemClickedAction>()

		val startLoadingNextDataAction = bottomScrolledAction
				.withLatestFrom(stateObservable, BiFunction { _: OnScrolledToBottomAction, state:InfinityState -> state })
				.filter { !it.isInfinityLoading && !it.isInfinityError }

		LoadingListBehaviour<Any, ToDoItem, InfinityState>(
				loadingTriggers = triggers(attachAction, retryInitialAction),
				loadWorker = ObservableWorker{ returnsSomeData(10, 0) },
				cancelPrevious = true,
				loadingMessage = messages({ ShowInitialLoading }),
				errorMessage = messages({ ShowInitialError }),
				emptyMessage = messages(),
				dataMessage = messages({ AddNewData(it) })
		).bindToView()

		InfinityLoadingBehaviour<Any, ToDoItem, InfinityState>(
				triggers = triggers(startLoadingNextDataAction, retryInfinityLoadingAction),
				loadWorker = ObservableWorker({ input -> returnsSomeData(input.limit, input.offset) }),
				limit = 10,
				initialOffset = 10,
				loadingMessage = messages({ ShowInfinityLoading }),
				completeMessage = messages(),
				errorMessage = messages({ ShowInfinityError }),
				dataMessage = messages({ AddNewData(it) })
		).bindToView()

		itemClickedAction
				.map { NavigateToDetailEvent }
				.bindToView()
	}

	private fun returnsSomeData(limit: Int, offset: Int): Observable<List<ToDoItem>> {
		return Observable.fromCallable {
			var list = listOf<ToDoItem>()
			if (offset > 50) {
				for (i in offset..(offset + 3)) {
					list = list.plus(ToDoItem(id = i, title = "Title $i"))
				}
			} else {
				for (i in offset..(offset + limit)) {
					list = list.plus(ToDoItem(id = i, title = "Title $i"))
				}
			}
			return@fromCallable list
		}
				.delay(5, TimeUnit.SECONDS)
				.flatMap {
					if (Random().nextInt(5) % 4 == 0) {
						return@flatMap Observable.error<List<ToDoItem>>(IllegalStateException("Error"))
					}
					return@flatMap Observable.just(it)
				}
	}

}