package com.sumera.koreactor.behaviour

import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class Triggers<DATA>(
        private val observables: List<Observable<out DATA>>
) {
    fun merge(): Observable<out DATA> {
        listOf<String>().map {  }

        return Observable.merge(observables)
                .onErrorResumeNext({ error: Throwable ->
                    Observable.error<DATA>(
                            IllegalStateException("Triggers should not contain any errors", error)
                    )
                })
    }

    fun <R> map(transform: (DATA) -> R): Triggers<R> {
        return Triggers(observables.map { it.map { transform(it) } })
    }

    operator fun plus(another: Triggers<DATA>): Triggers<DATA> {
        return Triggers(observables = this.observables + another.observables)
    }
}

fun <DATA> triggers(vararg observables: Observable<out DATA>): Triggers<DATA> {
    return Triggers(Arrays.asList(*observables))
}

fun <DATA> triggers(observable: Observable<out DATA>): Triggers<DATA> {
    return Triggers(Arrays.asList(observable))
}

fun <DATA> triggers(vararg singles: Single<DATA>): Triggers<DATA> {
    return Triggers(Arrays.asList(*singles).map { it.toObservable() })
}

fun <DATA> triggers(single: Single<DATA>): Triggers<DATA> {
    return Triggers(Arrays.asList(single).map { it.toObservable() })
}

fun <DATA> triggers(): Triggers<DATA> {
    return Triggers(listOf(Observable.never<DATA>()))
}
