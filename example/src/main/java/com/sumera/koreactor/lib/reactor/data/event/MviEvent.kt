package com.sumera.koreactor.lib.reactor.data.event

import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.reactor.data.MviStateReducer
import com.sumera.koreactor.lib.reactor.data.either.Either
import com.sumera.koreactor.lib.reactor.data.either.EitherEventOrReducer
import com.sumera.koreactor.lib.reactor.data.either.EitherLeft

interface MviEvent<STATE : MviState> : EitherEventOrReducer<STATE> {

	override val toEither: Either<MviEvent<STATE>, MviStateReducer<STATE>>
		get() = EitherLeft(this)

	fun eventBehaviour(): MviEventBehaviour = EveryTimeEventBehaviour
}
