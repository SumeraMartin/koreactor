package com.sumera.koreactor.view

import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.Observable

interface MviBindableView<STATE : MviState> {

	fun bindToState(stateObservable: Observable<STATE>) {

	}

	fun bindToEvent(eventsObservable: Observable<MviEvent<STATE>>) {

	}
}
