package com.sumera.koreactor.internal.util

import com.sumera.koreactor.reactor.data.AttachState
import com.sumera.koreactor.reactor.data.CreateState
import com.sumera.koreactor.reactor.data.DestroyState
import com.sumera.koreactor.reactor.data.DetachState
import com.sumera.koreactor.reactor.data.LifecycleState
import com.sumera.koreactor.reactor.data.PauseState
import com.sumera.koreactor.reactor.data.ResumeState
import com.sumera.koreactor.reactor.data.StartState
import com.sumera.koreactor.reactor.data.StopState

object LifecycleEventCorrectOrderValidator {

	fun isValidOrder(previous: LifecycleState?, current: LifecycleState): Boolean {
		if (previous == null) {
			return current is AttachState
		}

		return when(previous) {
			is AttachState -> current is CreateState
			is CreateState -> current is StartState || current is DestroyState
			is StartState -> current is ResumeState || current is StopState
			is ResumeState -> current is PauseState
			is PauseState -> current is StopState || current is ResumeState
			is StopState -> current is DestroyState || current is StartState
			is DestroyState -> current is CreateState || current is DetachState
			is DetachState -> false
		}
	}
}