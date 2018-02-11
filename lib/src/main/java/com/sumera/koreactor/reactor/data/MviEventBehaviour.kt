package com.sumera.koreactor.reactor.data

sealed class MviEventBehaviour {

	abstract val isRequiredViewVisibility: Boolean

	abstract val isCached: Boolean
}

object RequireStartedStateCached : MviEventBehaviour() {

	override val isRequiredViewVisibility = true

	override val isCached = true
}

object RequireStartedStateNotCached : MviEventBehaviour() {

	override val isRequiredViewVisibility = true

	override val isCached = false
}

object DispatchedEveryTime : MviEventBehaviour() {

	override val isRequiredViewVisibility = false

	override val isCached = false
}
