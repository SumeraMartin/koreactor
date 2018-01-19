package com.sumera.koreactor.lib.view

import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.event.MviEvent
import io.reactivex.Observable

interface MviBindableView<STATE : MviState> {

	fun bindToState(stateObservable: Observable<STATE>)

	fun bindToEvent(eventsObservable: Observable<MviEvent<STATE>>)

	fun unbindFromState()

	fun unbindFromEvents()
}
