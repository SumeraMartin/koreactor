package com.sumera.koreactor.lib.internal.extension

import com.sumera.koreactor.lib.reactor.lifecycle.*

fun LifecycleEvent.isViewStarted(): Boolean = when(this) {
	is AttachEvent -> false
	is CreateEvent -> false
	is StartEvent -> true
	is ResumeEvent -> true
	is PauseEvent -> true
	is StopEvent -> false
	is DestroyEvent -> false
	is DetachEvent -> false
}

fun LifecycleEvent.isViewCreated(): Boolean = when(this) {
	is AttachEvent -> false
	is CreateEvent -> true
	is StartEvent -> true
	is ResumeEvent -> true
	is PauseEvent -> true
	is StopEvent -> true
	is DestroyEvent -> false
	is DetachEvent -> false
}
