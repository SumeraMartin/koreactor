package com.sumera.koreactor.lib.reactor.data

import com.sumera.koreactor.lib.internal.data.Either
import com.sumera.koreactor.lib.internal.data.fold

interface EventOrReducer<STATE : MviState> : MviReactorMessage<STATE> {

	val toEither: Either<MviEvent<STATE>, MviStateReducer<STATE>>

	override fun getMessages(): Collection<EventOrReducer<STATE>> {
		return listOf(toEither.fold({ it }, { it }))
	}
}
