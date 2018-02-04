package com.sumera.koreactor.util.extension

import com.sumera.koreactor.reactor.lifecycle.LifecycleState
import io.reactivex.Observable

inline fun <reified R : LifecycleState> Observable<LifecycleState>.ofLifecycleType(): Observable<R> {
	return ofType(R::class.java)
}
