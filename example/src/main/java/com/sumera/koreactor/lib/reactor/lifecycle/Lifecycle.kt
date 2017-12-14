package com.sumera.koreactor.lib.reactor.lifecycle

sealed class LifecycleEvent {
	override fun toString(): String {
		return this::class.simpleName!!
	}
}

object AttachEvent : LifecycleEvent()

object CreateEvent : LifecycleEvent()

object StartEvent : LifecycleEvent()

object ResumeEvent : LifecycleEvent()

object PauseEvent : LifecycleEvent()

object StopEvent : LifecycleEvent()

object DestroyEvent : LifecycleEvent()

object DetachEvent : LifecycleEvent()
