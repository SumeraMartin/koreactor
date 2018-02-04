package com.sumera.koreactorlib.util.extension

import com.sumera.koreactorlib.reactor.lifecycle.LifecycleState
import io.reactivex.Observable

inline fun <reified R : LifecycleState> Observable<LifecycleState>.ofLifecycleType(): Observable<R> {
	return ofType(R::class.java)
}
