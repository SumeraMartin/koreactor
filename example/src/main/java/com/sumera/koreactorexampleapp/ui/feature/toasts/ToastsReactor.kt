package com.sumera.koreactorexampleapp.ui.feature.toasts

import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviAction
import com.sumera.koreactorexampleapp.injection.PerActivity
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.HideToastsActiveText
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.OnLastToastWasReceivedAction
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.OnTriggerToastsButtonClickAction
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.ShowToastEverytime
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.ShowToastOnlyVisible
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.ShowToastOnlyVisibleBuffered
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.ShowToastsActiveText
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.ToastsEvents
import com.sumera.koreactorexampleapp.ui.feature.toasts.contract.ToastsState
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerActivity
class ToastsReactor @Inject constructor() : MviReactor<ToastsState>() {

    override fun createInitialState(): ToastsState {
        return ToastsState(areToastsActive = false)
    }

    override fun bind(actions: Observable<MviAction<ToastsState>>) {
        val onTriggerButtonClickedAction = actions.ofActionType<OnTriggerToastsButtonClickAction>()
        val onLastToastsWasReceived = actions.ofActionType<OnLastToastWasReceivedAction>()

        // Show active toasts text
        onTriggerButtonClickedAction
                .map { ShowToastsActiveText }
                .bindToView()

        // Hide active toasts text
        onLastToastsWasReceived
                .map { HideToastsActiveText }
                .bindToView()

        // Trigger events
        onTriggerButtonClickedAction
                .switchMap { emitToastEvents() }
                .bindToView()
    }

    private fun emitToastEvents(): Observable<ToastsEvents> {
        val showAlwaysToast = Pair(2L, ShowToastEverytime("Shown every time"))
        val showOnlyVisibleToast = Pair(5L, ShowToastOnlyVisible("Shown only in the foreground state"))
        val showOnlyVisibleBufferedToast = Pair(8L, ShowToastOnlyVisibleBuffered("Shown only in the foreground state but buffered"))
        return Observable.just(showAlwaysToast, showOnlyVisibleToast, showOnlyVisibleBufferedToast)
                .flatMap { Observable.just(it.second).delay(it.first, TimeUnit.SECONDS) }
    }
}
