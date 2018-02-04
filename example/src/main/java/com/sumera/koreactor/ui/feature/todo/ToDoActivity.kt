package com.sumera.koreactor.ui.feature.todo

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.view.clicks
import com.sumera.koreactor.R
import com.sumera.koreactor.lib.reactor.MviReactor
import com.sumera.koreactor.lib.reactor.data.MviEvent
import com.sumera.koreactor.lib.util.data.asOptional
import com.sumera.koreactor.lib.util.extension.getChange
import com.sumera.koreactor.lib.util.extension.getTrue
import com.sumera.koreactor.ui.base.BaseActivity
import com.sumera.koreactor.ui.common.PlaceholderLayout
import com.sumera.koreactor.ui.feature.todo.adapter.ToDoAdapter
import com.sumera.koreactor.ui.feature.todo.contract.NavigateToSomewhereElse
import com.sumera.koreactor.ui.feature.todo.contract.OnAddItemAction
import com.sumera.koreactor.ui.feature.todo.contract.OnRetryAction
import com.sumera.koreactor.ui.feature.todo.contract.OnSwipeRefreshAction
import com.sumera.koreactor.ui.feature.todo.contract.OnToDoItemAction
import com.sumera.koreactor.ui.feature.todo.contract.OnToolbarIconClicked
import com.sumera.koreactor.ui.feature.todo.contract.ShowToastEverytime
import com.sumera.koreactor.ui.feature.todo.contract.ShowToastOnlyVisible
import com.sumera.koreactor.ui.feature.todo.contract.ShowToastOnlyVisibleBuffered
import com.sumera.koreactor.ui.feature.todo.contract.ToDoState
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_todo.*
import javax.inject.Inject

class ToDoActivity : BaseActivity<ToDoState>() {

	@Inject lateinit var viewModelFactory: ToDoReactorFactory
	@Inject lateinit var adapter: ToDoAdapter

	private var snackbar: Snackbar? = null

	override val layoutRes: Int
		get() = R.layout.activity_todo

	override fun createReactor(): MviReactor<ToDoState> {
		return getReactor(viewModelFactory, ToDoReactor::class.java)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		todo_recycler.layoutManager = LinearLayoutManager(this)
		todo_recycler.adapter = adapter

		// Recycler item clicked
		adapter.toDoItemClicks()
				.map { OnToDoItemAction(it) }
				.bindToReactor()

		// Swipe refresh start
		todo_swipeRefresh.refreshes()
				.map { OnSwipeRefreshAction }
				.bindToReactor()

		// Fab button clicked
		todo_plus.clicks()
				.map { OnAddItemAction }
				.bindToReactor()

		// Placeholder retry button clicked
		todo_placeholder.retryClicks()
				.map { OnRetryAction }
				.bindToReactor()

		// Toolbar icon clicked
		todo_toolbarIcon.clicks()
				.map { OnToolbarIconClicked }
				.bindToReactor()
	}

	override fun bindToState(stateObservable: Observable<ToDoState>) {

		// Show error layout
		stateObservable
				.getTrue { it.isError }
				.observeState { todo_placeholder.show(PlaceholderLayout.NETWORK_ERROR) }

		// Show loading layout
		stateObservable
				.getTrue { it.isLoading }
				.observeState { todo_placeholder.show(PlaceholderLayout.LOADING) }

		// Show swipe refresh layout
		stateObservable
				.getChange { it.isSwipeLoading }
				.observeState { todo_swipeRefresh.isRefreshing = it }

		// Show data
		stateObservable
				.getChange { it.data.asOptional() }
				.filter { it.value?.isNotEmpty() ?: false }
				.observeState {
					todo_placeholder.hide()
					adapter.data = it.value!!
				}

		// Show info message
		stateObservable
				.getChange({ it.infoMessage }, { it.isNotEmpty() })
				.observeState { showSnackBar(it) }

		// Hide info message
		stateObservable
				.getChange({ it.infoMessage }, { it.isEmpty() })
				.observeState { hideSnackbar() }
	}

	override fun bindToEvent(eventsObservable: Observable<MviEvent<ToDoState>>) {
		eventsObservable.observeEvent { event ->
			when(event) {
				is ShowToastEverytime ->
					Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
				is ShowToastOnlyVisible ->
					Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
				is ShowToastOnlyVisibleBuffered ->
					Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
				is NavigateToSomewhereElse ->
					finish()
			}
		}
	}

	fun showSnackBar(snackTitle: String) {
		snackbar = Snackbar.make(todo_contentRoot, snackTitle, Snackbar.LENGTH_INDEFINITE)
		snackbar?.apply { show() }
	}

	fun hideSnackbar() {
		snackbar?.apply { dismiss() }
	}
}