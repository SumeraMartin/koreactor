package com.sumera.koreactorexampleapp.ui.feature.todo

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.clicks
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.util.data.asOptional
import com.sumera.koreactor.util.extension.getChange
import com.sumera.koreactorexampleapp.R
import com.sumera.koreactorexampleapp.tools.extensions.isVisible
import com.sumera.koreactorexampleapp.ui.base.BaseActivity
import com.sumera.koreactorexampleapp.ui.feature.todo.adapter.ToDoAdapter
import com.sumera.koreactorexampleapp.ui.feature.todo.contract.OnAddItemAction
import com.sumera.koreactorexampleapp.ui.feature.todo.contract.OnToDoItemAction
import com.sumera.koreactorexampleapp.ui.feature.todo.contract.ToDoState
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

		// Fab button clicked
		todo_plus.clicks()
				.map { OnAddItemAction }
				.bindToReactor()
	}

	override fun bindToState(stateObservable: Observable<ToDoState>) {
		// Show data
		stateObservable
				.getChange { it.data.asOptional() }
				.filter { it.value?.isNotEmpty() ?: false }
				.observeState { adapter.data = it.value!! }

        // Show empty data
        stateObservable
                .getChange { it.data.asOptional() }
				.map { it.value?.isEmpty() ?: true }
                .observeState { todoViewHolder_empty.isVisible = it }

		// Show info message
		stateObservable
				.getChange({ it.infoMessage }, { it.isNotEmpty() })
				.observeState { showSnackBar(it) }

		// Hide info message
		stateObservable
				.getChange({ it.infoMessage }, { it.isEmpty() })
				.observeState { hideSnackBar() }
	}

	private fun showSnackBar(snackTitle: String) {
		snackbar = Snackbar.make(todo_contentRoot, snackTitle, Snackbar.LENGTH_INDEFINITE)
		snackbar?.apply { show() }
	}

	private fun hideSnackBar() {
		snackbar?.apply { dismiss() }
	}
}