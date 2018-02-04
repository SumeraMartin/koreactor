package com.sumera.koreactorlib.internal.extension

import com.sumera.koreactorlib.reactor.lifecycle.*

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
