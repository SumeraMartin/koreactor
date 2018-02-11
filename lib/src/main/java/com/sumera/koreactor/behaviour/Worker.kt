package com.sumera.koreactor.behaviour

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface Worker<in INPUT_DATA, out OUTPUT_DATA> {

    fun executeAsObservable(data: INPUT_DATA): Observable<out OUTPUT_DATA>
}

class ObservableWorker<in INPUT_DATA, out OUTPUT_DATA>(
        private val action: (INPUT_DATA) -> Observable<out OUTPUT_DATA>
): Worker<INPUT_DATA, OUTPUT_DATA> {

    override fun executeAsObservable(data: INPUT_DATA): Observable<out OUTPUT_DATA> {
        return action(data)
    }
}

class SingleWorker<in INPUT_DATA, out OUTPUT_DATA>(
        private val action: (INPUT_DATA) -> Single<out OUTPUT_DATA>
): Worker<INPUT_DATA, OUTPUT_DATA> {

    override fun executeAsObservable(data: INPUT_DATA): Observable<out OUTPUT_DATA> {
        return action(data).toObservable()
    }

    fun execute(data: INPUT_DATA): Single<out OUTPUT_DATA> {
        return action(data)
    }
}

class MaybeWorker<in INPUT_DATA, out OUTPUT_DATA>(
        private val action: (INPUT_DATA) -> Maybe<out OUTPUT_DATA>
): Worker<INPUT_DATA, OUTPUT_DATA> {

    override fun executeAsObservable(data: INPUT_DATA): Observable<out OUTPUT_DATA> {
        return action(data).toSingle().toObservable()
    }

    fun execute(data: INPUT_DATA): Maybe<out OUTPUT_DATA> {
        return action(data)
    }
}

class CompletableWorker<in INPUT_DATA>(
        private val action: (INPUT_DATA) -> Completable
): Worker<INPUT_DATA, Unit> {

    override fun executeAsObservable(data: INPUT_DATA): Observable<Unit> {
        return action(data)
                .toSingleDefault(Unit)
                .toObservable()
    }

    fun execute(data: INPUT_DATA): Completable {
        return action(data)
    }
}

fun <INPUT, OUTPUT> observable(action: (INPUT) -> Observable<out OUTPUT>): ObservableWorker<INPUT, OUTPUT> {
    return ObservableWorker(action)
}

fun <INPUT, OUTPUT> single(action: (INPUT) -> Single<out OUTPUT>): SingleWorker<INPUT, OUTPUT> {
    return SingleWorker(action)
}

fun <INPUT, OUTPUT> maybe(action: (INPUT) -> Maybe<out OUTPUT>): MaybeWorker<INPUT, OUTPUT> {
    return MaybeWorker(action)
}

fun <INPUT> completable(action: (INPUT) -> Completable): CompletableWorker<INPUT> {
    return CompletableWorker(action)
}
