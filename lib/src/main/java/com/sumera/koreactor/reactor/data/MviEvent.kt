package com.sumera.koreactor.reactor.data

import com.sumera.koreactor.internal.data.Either
import com.sumera.koreactor.internal.data.EitherLeft

interface MviEvent<STATE : MviState> : EventOrReducer<STATE>, MviReactorMessage<STATE> {

	fun eventBehaviour(): MviEventBehaviour = EveryTimeEventBehaviour

	override val toEither: Either<MviEvent<STATE>, MviStateReducer<STATE>>
		get() = EitherLeft(this)

	override fun getMessages(): Collection<EventOrReducer<STATE>> {
		return listOf(this)
	}
}
