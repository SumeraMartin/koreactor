package com.sumera.koreactor.internal.extension

import com.sumera.koreactor.internal.data.EventWithLifecycle
import com.sumera.koreactor.reactor.data.LifecycleState
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.reactor.data.MviState
import hu.akarnokd.rxjava2.operators.FlowableTransformers
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

fun <STATE : MviState> Observable<out MviEvent<STATE>>.cacheEventsUntilViewIsCreated(lifecycleObservable: Observable<LifecycleState>): Observable<MviEvent<STATE>> {
	return toFlowable(BackpressureStrategy.BUFFER)
			.compose(FlowableTransformers.valve(lifecycleObservable.map { it.isViewCreated() }.toFlowable(BackpressureStrategy.BUFFER)))
			.toObservable()
}

fun <STATE : MviState> Observable<out MviEvent<STATE>>.cacheEventsUntilViewIsStarted(lifecycleObservable: Observable<LifecycleState>): Observable<MviEvent<STATE>> {
	return toFlowable(BackpressureStrategy.BUFFER)
			.compose(FlowableTransformers.valve(lifecycleObservable.map { it.isViewStarted() }.toFlowable(BackpressureStrategy.BUFFER)))
			.toObservable()
}

fun <STATE : MviState> Observable<out MviEvent<STATE>>.throwEventsAwayIfViewIsNotStarted(lifecycleObservable: Observable<LifecycleState>): Observable<MviEvent<STATE>> {
	return withLatestFrom(lifecycleObservable, BiFunction { a: MviEvent<STATE>, b: LifecycleState -> EventWithLifecycle(a, b) })
			.filter { it.lifecycleState.isViewStarted() }
			.map { it.event }
}
