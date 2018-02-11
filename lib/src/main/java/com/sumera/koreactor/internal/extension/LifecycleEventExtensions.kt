package com.sumera.koreactor.internal.extension

import com.sumera.koreactor.reactor.data.AttachState
import com.sumera.koreactor.reactor.data.CreateState
import com.sumera.koreactor.reactor.data.DestroyState
import com.sumera.koreactor.reactor.data.DetachState
import com.sumera.koreactor.reactor.data.LifecycleState
import com.sumera.koreactor.reactor.data.PauseState
import com.sumera.koreactor.reactor.data.ResumeState
import com.sumera.koreactor.reactor.data.StartState
import com.sumera.koreactor.reactor.data.StopState

fun LifecycleState.isViewStarted(): Boolean = when(this) {
	is AttachState -> false
	is CreateState -> false
	is StartState -> true
	is ResumeState -> true
	is PauseState -> true
	is StopState -> false
	is DestroyState -> false
	is DetachState -> false
}

fun LifecycleState.isViewCreated(): Boolean = when(this) {
	is AttachState -> false
	is CreateState -> true
	is StartState -> true
	is ResumeState -> true
	is PauseState -> true
	is StopState -> true
	is DestroyState -> false
	is DetachState -> false
}
