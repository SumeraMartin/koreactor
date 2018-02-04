package com.sumera.koreactorlib.behaviour

import com.sumera.koreactorlib.reactor.data.MviReactorMessage
import com.sumera.koreactorlib.reactor.data.MviState
import io.reactivex.Observable

interface MviBehaviour<STATE : MviState> {

	fun createObservable(): Observable<MviReactorMessage<STATE>>
}
