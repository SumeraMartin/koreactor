package com.sumera.koreactor.reactor.data

import com.sumera.koreactor.internal.data.Either
import com.sumera.koreactor.internal.data.EitherRight

interface MviStateReducer<STATE : MviState> : EventOrReducer<STATE>, MviReactorMessage<STATE> {

	override val toEither: Either<MviEvent<STATE>, MviStateReducer<STATE>>
		get() = EitherRight(this)

	fun reduce(oldState: STATE) : STATE

	override fun getMessages(): Collection<EventOrReducer<STATE>> {
		return listOf(this)
	}
}
