package com.sumera.koreactor.lib.internal.util

import com.sumera.koreactor.lib.reactor.lifecycle.*

object LifecycleEventCorrectOrderValidator {

	fun isValidOrder(previous: LifecycleEvent?, current: LifecycleEvent): Boolean {
		if (previous == null) {
			return current is AttachEvent
		}

		return when(previous) {
			is AttachEvent -> current is CreateEvent
			is CreateEvent -> current is StartEvent
			is StartEvent -> current is ResumeEvent
			is ResumeEvent -> current is PauseEvent
			is PauseEvent -> current is StopEvent || current is ResumeEvent
			is StopEvent -> current is DestroyEvent || current is StartEvent
			is DestroyEvent -> current is CreateEvent || current is DetachEvent
			is DetachEvent -> throw IllegalStateException("No lifecycle events are allowed after DetachEvent: " + current)
		}
	}
}