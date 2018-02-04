package com.sumera.koreactor.internal.util

import com.sumera.koreactor.reactor.lifecycle.AttachState
import com.sumera.koreactor.reactor.lifecycle.CreateState
import com.sumera.koreactor.reactor.lifecycle.DestroyState
import com.sumera.koreactor.reactor.lifecycle.DetachState
import com.sumera.koreactor.reactor.lifecycle.LifecycleState
import com.sumera.koreactor.reactor.lifecycle.PauseState
import com.sumera.koreactor.reactor.lifecycle.ResumeState
import com.sumera.koreactor.reactor.lifecycle.StartState
import com.sumera.koreactor.reactor.lifecycle.StopState

object LifecycleEventCorrectOrderValidator {

	fun isValidOrder(previous: LifecycleState?, current: LifecycleState): Boolean {
		if (previous == null) {
			return current is AttachState
		}

		return when(previous) {
			is AttachState -> current is CreateState
			is CreateState -> current is StartState || current is DestroyState
			is StartState -> current is ResumeState
			is ResumeState -> current is PauseState
			is PauseState -> current is StopState || current is ResumeState
			is StopState -> current is DestroyState || current is StartState
			is DestroyState -> current is CreateState || current is DetachState
			is DetachState -> throw IllegalStateException("No lifecycle events are allowed after DetachEvent: " + current)
		}
	}
}