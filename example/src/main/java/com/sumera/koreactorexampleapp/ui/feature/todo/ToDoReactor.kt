package com.sumera.koreactorexampleapp.ui.feature.todo

import com.sumera.koreactor.behaviour.implementation.LoadingBehaviour
import com.sumera.koreactor.behaviour.messages
import com.sumera.koreactor.behaviour.single
import com.sumera.koreactor.behaviour.triggers
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.util.bundle.BundleWrapper
import com.sumera.koreactorexampleapp.data.ToDoItem
import com.sumera.koreactorexampleapp.domain.SaveToDoItemInteractor
import com.sumera.koreactorexampleapp.injection.PerActivity
import com.sumera.koreactorexampleapp.ui.feature.todo.adapter.ToDoItemWrapper
import com.sumera.koreactorexampleapp.ui.feature.todo.contract.AddToDoItemReducer
import com.sumera.koreactorexampleapp.ui.feature.todo.contract.HideInfoReducer
import com.sumera.koreactorexampleapp.ui.feature.todo.contract.OnAddItemAction
import com.sumera.koreactorexampleapp.ui.feature.todo.contract.OnToDoItemAction
import com.sumera.koreactorexampleapp.ui.feature.todo.contract.ShowInfoReducer
import com.sumera.koreactorexampleapp.ui.feature.todo.contract.ShowToDoItemCompletedReducer
import com.sumera.koreactorexampleapp.ui.feature.todo.contract.ShowToDoItemLoadingReducer
import com.sumera.koreactorexampleapp.ui.feature.todo.contract.ToDoState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerActivity
class ToDoReactor @Inject constructor(
		private val saveToDoItemInteractor: SaveToDoItemInteractor
) : MviReactor<ToDoState>() {

	companion object {
	    private const val DATA_KEY = "data_key"
	}

	override fun createInitialState(): ToDoState {
		return ToDoState(
				infoMessage = "",
				data = null)
	}

	override fun onSaveInstanceState(state: ToDoState, bundleWrapper: BundleWrapper) {
		val data = state.data?.map { it.copyWithCancelLoadingState() }
		bundleWrapper.putParcelableArray(DATA_KEY, data?.toTypedArray())
	}

	override fun onRestoreSaveInstanceState(state: ToDoState, bundleWrapper: BundleWrapper): ToDoState {
		val data = bundleWrapper.getParcelableArray(DATA_KEY)?.toList() as List<ToDoItemWrapper>
		return state.copy(data = data)
	}

	override fun bind(actions: Observable<MviAction<ToDoState>>) {
		val addItemAction = actions.ofActionType<OnAddItemAction>()
		val itemClickedAction = actions.ofActionType<OnToDoItemAction>()

		LoadingBehaviour<ToDoItem, ToDoItem, ToDoState>(
				triggers = triggers(itemClickedAction.map { it.toDoItemWrapper.toDoItem }),
				loadWorker = single { saveToDoItemInteractor.init(it).execute() },
				cancelPrevious = false,
				loadingMessage = messages { ShowToDoItemLoadingReducer(it.id) },
				errorMessage = messages(), // There are no errors in this stream
				dataMessage = messages { ShowToDoItemCompletedReducer(it.id) }
		).bindToView()

		addItemAction
				.addNewToDoItem()
				.andThenShowTemporaryInfoMessage()
				.bindToView()
	}

	private fun Observable<OnAddItemAction>.addNewToDoItem(): Observable<AddToDoItemReducer> {
		return this.flatMapSingle { stateSingle.map { AddToDoItemReducer(generateNewId(it)) } }
	}

	private fun Observable<AddToDoItemReducer>.andThenShowTemporaryInfoMessage(): Observable<MviReactorMessage<ToDoState>> {
		return this.switchMap { addToItem ->
			Observable.just(Unit)
					.delay(3, TimeUnit.SECONDS)
					.map<MviReactorMessage<ToDoState>> { HideInfoReducer }
					.startWithArray(addToItem, ShowInfoReducer("Item added ${addToItem.newItemId}"))
		}
	}

	private fun generateNewId(state: ToDoState) : Int {
		val max = state.data?.map { it.toDoItem.id }?.max() ?: 0
		return max + 1
	}
}