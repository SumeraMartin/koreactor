package com.sumera.koreactorlib.behaviour

import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class Triggers<DATA>(val observables: List<Observable<out DATA>> = listOf())

fun <DATA> triggers(vararg observables: Observable<out DATA>): Triggers<DATA> {
    return Triggers(Arrays.asList(*observables))
}

fun <DATA> triggers(vararg singles: Single<DATA>): Triggers<out DATA> {
    return Triggers(Arrays.asList(*singles).map { it.toObservable() })
}
