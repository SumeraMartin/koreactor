package com.sumera.koreactor.behaviour

import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import io.reactivex.Observable

interface MviBehaviour<STATE : MviState> {

	fun createObservable(): Observable<out MviReactorMessage<STATE>>
}
