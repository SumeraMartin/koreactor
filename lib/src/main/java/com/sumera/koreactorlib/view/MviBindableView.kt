package com.sumera.koreactorlib.view

import com.sumera.koreactorlib.reactor.data.MviEvent
import com.sumera.koreactorlib.reactor.data.MviState
import io.reactivex.Observable

interface MviBindableView<STATE : MviState> {

	fun bindToState(stateObservable: Observable<STATE>) {

	}

	fun bindToEvent(eventsObservable: Observable<MviEvent<STATE>>) {

	}
}
