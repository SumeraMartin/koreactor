package com.sumera.koreactorexampleapp.ui.feature.toasts

import android.os.Bundle
import android.widget.Toast
import com.jakewharton.rxbinding2.view.clicks
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.util.extension.getChange
import com.sumera.koreactorexampleapp.R
import com.sumera.koreactorexampleapp.tools.extensions.isVisible
import com.sumera.koreactorexampleapp.ui.base.BaseActivity
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.OnLastToastWasReceivedAction
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.OnTriggerToastsButtonClickAction
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.ShowToastEverytime
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.ShowToastOnlyVisible
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.ShowToastOnlyVisibleBuffered
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.ToastsState
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_toasts.*
import javax.inject.Inject

class ToastsActivity : BaseActivity<ToastsState>() {

    @Inject lateinit var reactorFactory: ToastsReactorFactory

    override val layoutRes: Int
        get() = R.layout.activity_toasts

    override fun createReactor(): MviReactor<ToastsState> {
        return getReactor(reactorFactory, ToastsReactor::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toasts_button.clicks()
                .map { OnTriggerToastsButtonClickAction }
                .bindToReactor()
    }

    override fun bindToState(stateObservable: Observable<ToastsState>) {
        // Show/hide active text when toasts are active
        stateObservable.getChange { it.areToastsActive }
                .observeState { toasts_activeText.isVisible = it }

        // Enable/disable button when toasts are active
        stateObservable.getChange { it.areToastsActive }
                .observeState { areToastsActive ->
                    toasts_button.isEnabled = !areToastsActive
                }
    }

    override fun bindToEvent(eventsObservable: Observable<MviEvent<ToastsState>>) {
        eventsObservable.observeEvent { event ->
            when (event) {
                is ShowToastEverytime ->
                    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
                is ShowToastOnlyVisible ->
                    Toast.makeText(this, event.message, Toast.LENGTH_LONG).show()
                is ShowToastOnlyVisibleBuffered -> {
                    Toast.makeText(this, event.message, Toast.LENGTH_LONG).show()
                    sendAction(OnLastToastWasReceivedAction)
                }
            }
        }
    }
}
