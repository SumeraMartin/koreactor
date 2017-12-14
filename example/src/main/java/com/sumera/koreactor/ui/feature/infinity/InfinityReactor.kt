package com.sumera.koreactor.ui.feature.todo

import com.sumera.koreactor.data.ToDoItem
import com.sumera.koreactor.lib.behaviour.implementation.InfinityLoadingBehaviour
import com.sumera.koreactor.lib.behaviour.implementation.LoadingListBehaviour
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.lib.reactor.lifecycle.AttachEvent
import com.sumera.koreactor.lib.util.extension.ofLifecycleType
import com.sumera.koreactor.ui.feature.infinity.contract.*
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
		val attachAction = lifecycleObservable.ofLifecycleType<AttachEvent>()
		val retryInitialAction = actions.ofActionType<OnRetryInitialAction>()
		val bottomScrolledAction = actions.ofActionType<OnScrolledToBottomAction>()
		val retryInfinityLoadingAction = actions.ofActionType<OnRetryInfinityLoadingAction>()
		val itemClickedAction = actions.ofActionType<OnItemClickedAction>()

		val startLoadingNextDataAction = bottomScrolledAction
				.withLatestFrom(stateObservable, BiFunction { _: OnScrolledToBottomAction, state:InfinityState -> state })
				.filter { !it.isInfinityLoading && !it.isInfinityError }

		LoadingListBehaviour(
				loadingObservables = listOf(attachAction, retryInitialAction),
				loadDataAction = { returnsSomeData(10, 0) },
				cancelPrevious = true,
				showLoadingReducer = { ShowInitialLoading },
				showErrorReducer = { ShowInitialError },
				showDataReducer = { AddNewData(it) }
		).bindToView()

		InfinityLoadingBehaviour(
				startLoadingObservables = listOf(startLoadingNextDataAction, retryInfinityLoadingAction),
				loadDataAction = { input, limit, offset -> returnsSomeData(limit, offset) },
				limit = 10,
				offset = 10,
				showLoadingReducer = { ShowInfinityLoading },
				showErrorReducer = { ShowInfinityError },
				showDataReducer = { AddNewData(it) }
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