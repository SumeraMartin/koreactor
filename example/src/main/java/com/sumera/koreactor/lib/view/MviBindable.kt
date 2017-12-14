package com.sumera.koreactor.lib.view

import com.sumera.koreactor.lib.reactor.data.event.MviEvent
import com.sumera.koreactor.lib.reactor.data.MviState
import io.reactivex.Observable

interface MviBindable<STATE : MviState> {

	fun bindToState(stateObservable: Observable<STATE>)

	fun bindToEvent(eventsObservable: Observable<MviEvent<STATE>>)
}
