package com.sumera.koreactor.lib.reactor.data

import com.sumera.koreactor.lib.reactor.data.either.Either
import com.sumera.koreactor.lib.reactor.data.either.EitherEventOrReducer
import com.sumera.koreactor.lib.reactor.data.either.EitherRight
import com.sumera.koreactor.lib.reactor.data.event.MviEvent

interface MviStateReducer<STATE : MviState> : EitherEventOrReducer<STATE> {

	override val toEither: Either<MviEvent<STATE>, MviStateReducer<STATE>>
		get() = EitherRight(this)

	fun reduce(oldState: STATE) : STATE
}
