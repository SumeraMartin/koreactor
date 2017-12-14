package com.sumera.koreactor.lib.reactor.data.event

sealed class MviEventBehaviour {

	abstract val isRequiredViewVisibility: Boolean

	abstract val isBuffered: Boolean
}

object VisibleBufferedEventBehaviour : MviEventBehaviour() {

	override val isRequiredViewVisibility = true

	override val isBuffered = true
}

object VisibleNonBufferedEventBehaviour : MviEventBehaviour() {

	override val isRequiredViewVisibility = true

	override val isBuffered = false
}

object EveryTimeEventBehaviour : MviEventBehaviour() {

	override val isRequiredViewVisibility = false

	override val isBuffered = false
}
