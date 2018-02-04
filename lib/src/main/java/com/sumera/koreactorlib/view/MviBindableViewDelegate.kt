package com.sumera.koreactorlib.view

import com.sumera.koreactorlib.reactor.data.MviEvent
import com.sumera.koreactorlib.reactor.data.MviState
import io.reactivex.Observable

interface MviBindableViewDelegate<STATE : MviState> {

	fun bindToState(stateObservable: Observable<STATE>)

	fun bindToEvent(eventsObservable: Observable<MviEvent<STATE>>)

	fun unbindFromState()

	fun unbindFromEvents()

	fun unbindActions()
}
