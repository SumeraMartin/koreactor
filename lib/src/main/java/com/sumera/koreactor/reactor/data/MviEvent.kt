package com.sumera.koreactor.reactor.data

import com.sumera.koreactor.internal.data.Either
import com.sumera.koreactor.internal.data.EitherLeft
import com.sumera.koreactor.internal.data.EventOrReducer

open class MviEvent<STATE : MviState> : EventOrReducer<STATE>, MviReactorMessage<STATE> {

	open val eventBehaviour: MviEventBehaviour = DispatchedEveryTime

	override val toEither: Either<MviEvent<STATE>, MviStateReducer<STATE>>
		get() = EitherLeft(this)

	override fun messages(): Collection<EventOrReducer<STATE>> {
		return listOf(this)
	}
}
