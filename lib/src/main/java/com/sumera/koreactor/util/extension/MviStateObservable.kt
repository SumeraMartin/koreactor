package com.sumera.koreactor.util.extension

import com.sumera.koreactor.reactor.data.MviState
import com.sumera.koreactor.util.data.Optional
import io.reactivex.Observable

fun <STATE : MviState, OUT> Observable<out STATE>.getChange(action: (STATE) -> (OUT)) : Observable<OUT> {
	return map { action(it) }
			.distinctUntilChanged()
}

fun <STATE : MviState, OUT> Observable<out STATE>.getChange(action: (STATE) -> (OUT), filter: (OUT) -> Boolean) : Observable<OUT> {
	return map { action.invoke(it) }
			.distinctUntilChanged()
			.filter(filter)
}

fun <STATE : MviState, OUT> Observable<out STATE>.getNotNull(action: (STATE) -> (Optional<OUT>)) : Observable<OUT> {
	return getChange(action)
			.filter { it.value != null }
			.map { it.value!! }
}

fun <STATE : MviState, OUT> Observable<out STATE>.getNotNull(action: (STATE) -> (Optional<OUT>), filter: (OUT) -> Boolean) : Observable<OUT> {
	return getChange(action)
			.filter { it.value != null }
			.map { it.value!! }
			.filter(filter)
}

fun <STATE : MviState, OUT> Observable<out STATE>.getNull(action: (STATE) -> (Optional<OUT>)) : Observable<Unit> {
	return getChange(action)
			.filter { it.value == null }
			.map { Unit }
}

fun <STATE : MviState> Observable<out STATE>.getTrue(action: (STATE) -> (Boolean)) : Observable<Boolean> {
	return getChange(action)
			.filter { it }
}

fun <STATE : MviState> Observable<out STATE>.getFalse(action: (STATE) -> (Boolean)) : Observable<Boolean> {
	return getChange(action)
			.filter { !it }
}