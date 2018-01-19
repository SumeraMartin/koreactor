package com.sumera.koreactor.lib.internal.util

import com.sumera.koreactor.lib.reactor.lifecycle.*

object LifecycleEventCorrectOrderValidator {

	fun isValidOrder(previous: LifecycleState?, current: LifecycleState): Boolean {
		if (previous == null) {
			return current is AttachState
		}

		return when(previous) {
			is AttachState -> current is CreateState
			is CreateState -> current is StartState
			is StartState -> current is ResumeState
			is ResumeState -> current is PauseState
			is PauseState -> current is StopState || current is ResumeState
			is StopState -> current is DestroyState || current is StartState
			is DestroyState -> current is CreateState || current is DetachState
			is DetachState -> throw IllegalStateException("No lifecycle events are allowed after DetachEvent: " + current)
		}
	}
}