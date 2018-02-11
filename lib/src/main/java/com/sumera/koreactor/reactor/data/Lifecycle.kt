package com.sumera.koreactor.reactor.data

sealed class LifecycleState {
	override fun toString(): String {
		return this::class.simpleName!!
	}
}

object AttachState : LifecycleState()

object CreateState : LifecycleState()

object StartState : LifecycleState()

object ResumeState : LifecycleState()

object PauseState : LifecycleState()

object StopState : LifecycleState()

object DestroyState : LifecycleState()

object DetachState : LifecycleState()
