package com.sumera.koreactor.lib.behaviour

import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.either.EitherEventOrReducer
import io.reactivex.Observable

interface MviBehaviour<STATE : MviState> {

	fun createObservable(): Observable<EitherEventOrReducer<STATE>>
}
