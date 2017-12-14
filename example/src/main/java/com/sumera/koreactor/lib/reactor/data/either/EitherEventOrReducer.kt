package com.sumera.koreactor.lib.reactor.data.either

import com.sumera.koreactor.lib.reactor.data.event.MviEvent
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.MviStateReducer

interface EitherEventOrReducer<STATE : MviState> {

	val toEither: Either<MviEvent<STATE>, MviStateReducer<STATE>>
}
