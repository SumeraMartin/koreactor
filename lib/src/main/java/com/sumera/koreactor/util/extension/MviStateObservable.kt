package com.sumera.koreactor.util.extension

import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactor.util.data.Optional
import io.reactivex.Observable

fun <T : MviState, V> Observable<out T>.getChange(action: (T) -> (V)) : Observable<V> {
	return map { action.invoke(it) }
			.distinctUntilChanged()
}

fun <T : MviState, V> Observable<out T>.getChange(action: (T) -> (V), filter: (V) -> Boolean) : Observable<V> {
	return map { action.invoke(it) }
			.distinctUntilChanged()
			.filter(filter)
}

fun <T : MviState, V> Observable<out T>.getNotNull(action: (T) -> (Optional<V>)) : Observable<V> {
	return getChange(action)
			.filter { it.value != null }
			.map { it.value!! }
}

fun <T : MviState, V> Observable<out T>.getNotNull(action: (T) -> (Optional<V>), filter: (V) -> Boolean) : Observable<V> {
	return getChange(action)
			.filter { it.value != null }
			.map { it.value!! }
			.filter(filter)
}

fun <T : MviState, V> Observable<out T>.getNull(action: (T) -> (Optional<V>)) : Observable<Unit> {
	return getChange(action)
			.filter { it.value == null }
			.map { Unit }
}

fun <T : MviState> Observable<out T>.getTrue(action: (T) -> (Boolean)) : Observable<Boolean> {
	return getChange(action)
			.filter { it }
}

fun <T : MviState> Observable<out T>.getFalse(action: (T) -> (Boolean)) : Observable<Boolean> {
	return getChange(action)
			.filter { !it }
}