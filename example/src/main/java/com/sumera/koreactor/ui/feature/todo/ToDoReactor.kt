package com.sumera.koreactor.ui.feature.todo

import com.sumera.koreactorlib.behaviour.ObservableWorker
import com.sumera.koreactorlib.behaviour.implementation.LoadingBehaviour
import com.sumera.koreactorlib.behaviour.implementation.ShowTemporaryBehaviour
import com.sumera.koreactorlib.behaviour.implementation.SwipeRefreshLoadingListBehaviour
import com.sumera.koreactorlib.behaviour.messages
import com.sumera.koreactorlib.behaviour.triggers
import com.sumera.koreactor.data.ToDoItem
import com.sumera.koreactor.domain.GetToDoItemsOnceInteractor
import com.sumera.koreactor.domain.SaveToDoItemInteractor
import com.sumera.koreactor.injection.PerActivity
import com.sumera.koreactorlib.reactor.MviReactor
import com.sumera.koreactorlib.reactor.data.MviAction
import com.sumera.koreactorlib.reactor.lifecycle.AttachState
import com.sumera.koreactor.ui.feature.todo.adapter.ToDoItemWrapper
import com.sumera.koreactor.ui.feature.todo.contract.AddToDoItem
import com.sumera.koreactor.ui.feature.todo.contract.HideInfoMessage
import com.sumera.koreactor.ui.feature.todo.contract.OnAddItemAction
import com.sumera.koreactor.ui.feature.todo.contract.OnRetryAction
import com.sumera.koreactor.ui.feature.todo.contract.OnSwipeRefreshAction
import com.sumera.koreactor.ui.feature.todo.contract.OnToDoItemAction
import com.sumera.koreactor.ui.feature.todo.contract.OnToolbarIconClicked
import com.sumera.koreactor.ui.feature.todo.contract.ShowData
import com.sumera.koreactor.ui.feature.todo.contract.ShowEmpty
import com.sumera.koreactor.ui.feature.todo.contract.ShowError
import com.sumera.koreactor.ui.feature.todo.contract.ShowInfoMessage
import com.sumera.koreactor.ui.feature.todo.contract.ShowLoading
import com.sumera.koreactor.ui.feature.todo.contract.ShowSwipeRefreshLoading
import com.sumera.koreactor.ui.feature.todo.contract.ShowToDoItemCompleted
import com.sumera.koreactor.ui.feature.todo.contract.ShowToDoItemLoading
import com.sumera.koreactor.ui.feature.todo.contract.ShowToastEverytime
import com.sumera.koreactor.ui.feature.todo.contract.ShowToastOnlyVisible
import com.sumera.koreactor.ui.feature.todo.contract.ShowToastOnlyVisibleBuffered
import com.sumera.koreactor.ui.feature.todo.contract.ToDoState
import com.sumera.koreactorlib.util.extension.ofLifecycleType
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerActivity
class ToDoReactor @Inject constructor(
		private val getToDoItemsOnceInteractor: GetToDoItemsOnceInteractor,
		private val saveToDoItemInteractor: SaveToDoItemInteractor
) : MviReactor<ToDoState>() {

	private val showErrorMessageAction = PublishSubject.create<String>()

	override fun createInitialState(): ToDoState {
		return ToDoState(
				isLoading = true,
				isSwipeLoading = false,
				isError = false,
				infoMessage = "",
				data = null)
	}

	override fun bind(actions: Observable<MviAction<ToDoState>>) {
		val attachAction = lifecycleObservable.ofLifecycleType<AttachState>()
		val refreshAction = actions.ofActionType<OnSwipeRefreshAction>()
		val retryAction = actions.ofActionType<OnRetryAction>()
		val addItemAction = actions.ofActionType<OnAddItemAction>()
		val itemClickedAction = actions.ofActionType<OnToDoItemAction>()
		val toolbarItemClickedAction = actions.ofActionType<OnToolbarIconClicked>()

		val showInfoAction = PublishSubject.create<Int>()

		getToDoItemsOnceInteractor.execute().map { ShowLoading }
				.bindToView()

		SwipeRefreshLoadingListBehaviour<Any, ToDoItemWrapper, ToDoState>(
				initialLoadingTriggers = triggers(attachAction, refreshAction),
				swipeRefreshTriggers = triggers(retryAction),
				loadWorker = ObservableWorker{ getToDoItemsOnceInteractor.execute() },
				cancelPrevious = true,
				loadingMessage = messages({ ShowLoading }),
				swipeRefreshMessage = messages({ ShowSwipeRefreshLoading }),
				errorMessage = messages({ ShowError }),
				emptyMessage = messages({ ShowEmpty }),
				dataMessage = messages({ ShowData(it) })
		).bindToView()

		ShowTemporaryBehaviour<Any, ToDoState>(
				triggers = triggers(showInfoAction),
				duration = 3,
				timeUnit = TimeUnit.SECONDS,
				cancelPrevious = true,
				startMessage = messages({ ShowInfoMessage("Item added " + it) }),
				endMessage = messages({ HideInfoMessage })
		).bindToView()

		LoadingBehaviour<ToDoItem, ToDoItem, ToDoState>(
				triggers = triggers(itemClickedAction.map { it.toDoItemWrapper.toDoItem }),
				loadWorker = ObservableWorker{ saveToDoItemInteractor.init(it).execute() },
				cancelPrevious = false,
				loadingMessage = messages({ ShowToDoItemLoading(it.id) }),
				errorMessage = messages({ ShowError }),
				dataMessage = messages({ ShowToDoItemCompleted(it.id) })
		).bindToView()

		addItemAction
				.flatMap { getToDoItemsOnceInteractor.execute() }
				.flatMapSingle { stateSingle.map { AddToDoItem(generateNewId(it)) } }
				.doOnNext { showInfoAction.onNext(it.newItemId) }
				.bindToView()

		toolbarItemClickedAction
				.flatMap {
					Observable.just(
							Pair(2L, ShowToastEverytime("Coool toast 1")),
							Pair(5L, ShowToastOnlyVisible("Coool toast 2")),
							Pair(8L, ShowToastOnlyVisibleBuffered("Coool toast 3")))
							.flatMap { Observable.just(it).delay(it.first, TimeUnit.SECONDS) }
				}
				.map { it.second }
				.bindToView()
	}

	private fun generateNewId(state: ToDoState) : Int {
		if (state.data == null) {
			return 1
		}
		val max = state.data.map { it.toDoItem.id }.max() ?: 0
		return max + 1
	}
}