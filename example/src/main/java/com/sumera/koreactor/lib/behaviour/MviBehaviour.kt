package com.sumera.koreactor.lib.behaviour

import com.sumera.koreactor.lib.reactor.data.MviReactorMessage
import com.sumera.koreactor.lib.reactor.data.MviState
import io.reactivex.Observable

interface MviBehaviour<STATE : MviState> {

	fun createObservable(): Observable<MviReactorMessage<STATE>>
}
