package com.sumera.koreactor.lib.util.extension

import com.sumera.koreactor.lib.reactor.lifecycle.LifecycleEvent
import io.reactivex.Observable

inline fun <reified R : LifecycleEvent> Observable<LifecycleEvent>.ofLifecycleType(): Observable<R> {
	return ofType(R::class.java)
}
