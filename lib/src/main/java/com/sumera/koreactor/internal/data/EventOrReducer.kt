package com.sumera.koreactor.internal.data

import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviReactorMessage
import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactor.reactor.data.MviStateReducer

interface EventOrReducer<STATE : MviState> : MviReactorMessage<STATE> {

	val toEither: Either<MviEvent<STATE>, MviStateReducer<STATE>>

	override fun messages(): Collection<EventOrReducer<STATE>> {
		return listOf(toEither.fold({ it }, { it }))
	}
}
