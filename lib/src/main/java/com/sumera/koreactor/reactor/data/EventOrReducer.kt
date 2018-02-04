package com.sumera.koreactor.reactor.data

import com.sumera.koreactor.internal.data.Either
import com.sumera.koreactor.internal.data.fold

interface EventOrReducer<STATE : MviState> : MviReactorMessage<STATE> {

	val toEither: Either<MviEvent<STATE>, MviStateReducer<STATE>>

	override fun getMessages(): Collection<EventOrReducer<STATE>> {
		return listOf(toEither.fold({ it }, { it }))
	}
}
