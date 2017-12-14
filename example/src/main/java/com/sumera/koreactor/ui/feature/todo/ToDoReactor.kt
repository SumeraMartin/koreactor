package com.sumera.koreactor.ui.feature.todo

import com.sumera.koreactor.domain.GetToDoItemsOnceInteractor
import com.sumera.koreactor.domain.SaveToDoItemInteractor
import com.sumera.koreactor.lib.behaviour.implementation.LoadingBehaviour
import com.sumera.koreactor.lib.behaviour.implementation.ShowTemporaryBehaviour
import com.sumera.koreactor.lib.behaviour.implementation.SwipeRefreshLoadingListBehaviour
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviAction
import com.sumera.koreactor.lib.reactor.lifecycle.AttachEvent
import com.sumera.koreactor.lib.util.extension.ofLifecycleType
import com.sumera.koreactor.ui.feature.todo.contract.*
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
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
		val attachAction = lifecycleObservable.ofLifecycleType<AttachEvent>()
		val refreshAction = actions.ofActionType<OnSwipeRefreshAction>()
		val retryAction = actions.ofActionType<OnRetryAction>()
		val addItemAction = actions.ofActionType<OnAddItemAction>()
		val itemClickedAction = actions.ofActionType<OnToDoItemAction>()
		val toolbarItemClickedAction = actions.ofActionType<OnToolbarIconClicked>()

		val showInfoAction = PublishSubject.create<Int>()

		getToDoItemsOnceInteractor.execute().map { ShowLoading }
				.bindToView()

		SwipeRefreshLoadingListBehaviour(
				loadingObservables = listOf(attachAction, refreshAction),
				swipeRefreshObservables = listOf(retryAction),
				loadDataAction = { getToDoItemsOnceInteractor.execute() },
				cancelPrevious = true,
				showLoadingReducer = { ShowLoading },
				showSwipeRefreshReducer = { ShowSwipeRefreshLoading },
				showErrorReducer = { ShowError },
				showEmptyReducer = { ShowEmpty },
				showDataReducer = { ShowData(it) }
		).bindToView()

		ShowTemporaryBehaviour(
				startAction = listOf(showInfoAction),
				duration = 3,
				timeUnit = TimeUnit.SECONDS,
				cancelPrevious = true,
				startReducer = { ShowInfoMessage("Item added " + it) },
				endReducer = { HideInfoMessage }
		).bindToView()

		LoadingBehaviour(
				loadingObservables = listOf(itemClickedAction.map { it.toDoItemWrapper.toDoItem }),
				loadDataAction = { saveToDoItemInteractor.init(it).execute() },
				cancelPrevious = false,
				showLoadingReducer = { ShowToDoItemLoading(it.id) },
				showErrorReducer = { ShowError },
				showDataReducer = { ShowToDoItemCompleted(it.id) }
		).bindToView()

		addItemAction
				.flatMap { getToDoItemsOnceInteractor.execute() }
				.flatMap { stateObservableOnce.map { AddToDoItem(generateNewId(it)) } }
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

//		Observable.merge(attachAction, retryAction, refreshAction)
//				.switchMap { loadMovies(it) }
//				.bindToView()
//
//
//		itemClickedAction
//				.flatMap { saveMovie(it.toDoItemWrapper.toDoItem)}
//				.bindToView()
	}

//	private fun loadMovies(inputValue: Any): Observable<out MviStateReducer<ToDoState>> {
//		val loadMovies = getToDoItemsOnceInteractor.execute()
//				.map { if (it.isNotEmpty()) ShowData(it) else ShowEmpty }
//				.onErrorReturn { ShowError }
//		if (inputValue is OnSwipeRefreshAction) {
//			return loadMovies.startWith(ShowSwipeRefreshLoading)
//		}
//		return loadMovies.startWith(ShowSwipeRefreshLoading)
//	}
//
//	private fun saveMovie(toDoitem: ToDoItemWrapper): Observable<out MviStateReducer<ToDoState>> {
//		val id = toDoitem.id
//		return saveToDoItemInteractor.init(toDoitem).execute()
//				.map { ShowToDoItemCompleted(it.toDoItem.id) as ToDoStateReducer }
//				.doOnError { showErrorMessageAction.onNext("Something went wrong") }
//				.onErrorReturn { ShowError }
//				.startWith(ShowToDoItemLoading(id))
//	}

	private fun generateNewId(state: ToDoState) : Int {
		if (state.data == null) {
			return 1
		}
		val max = state.data.map { it.toDoItem.id }.max() ?: 0
		return max + 1
	}
}