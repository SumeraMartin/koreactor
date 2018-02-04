package com.sumera.koreactor.behaviour

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface Worker<INPUT_DATA, OUTPUT_DATA> {

    fun execute(data: INPUT_DATA): Observable<out OUTPUT_DATA>
}

class ObservableWorker<INPUT_DATA, OUTPUT_DATA>(
        private val action: (INPUT_DATA) -> Observable<out OUTPUT_DATA>
): Worker<INPUT_DATA, OUTPUT_DATA> {

    override fun execute(data: INPUT_DATA): Observable<out OUTPUT_DATA> {
        return action(data)
    }
}

class SingleWorker<INPUT_DATA, OUTPUT_DATA>(
        private val action: (INPUT_DATA) -> Single<out OUTPUT_DATA>
): Worker<INPUT_DATA, OUTPUT_DATA> {

    override fun execute(data: INPUT_DATA): Observable<out OUTPUT_DATA> {
        return action(data).toObservable()
    }
}

class CompletableWorker<INPUT_DATA>(
        private val action: (INPUT_DATA) -> Completable
): Worker<INPUT_DATA, Unit> {

    override fun execute(data: INPUT_DATA): Observable<Unit> {
        return action(data)
                .toSingleDefault(Unit)
                .toObservable()
    }
}

class FlowableWorker<INPUT_DATA, OUTPUT_DATA>(
        private val action: (INPUT_DATA) -> Flowable<out OUTPUT_DATA>
): Worker<INPUT_DATA, OUTPUT_DATA> {

    override fun execute(data: INPUT_DATA): Observable<out OUTPUT_DATA> {
        return action(data).toObservable()
    }
}

fun <INPUT, OUTPUT> flowable(action: (INPUT) -> Flowable<out OUTPUT>): FlowableWorker<INPUT, OUTPUT> {
    return FlowableWorker(action)
}

fun <INPUT, OUTPUT> observable(action: (INPUT) -> Observable<out OUTPUT>): ObservableWorker<INPUT, OUTPUT> {
    return ObservableWorker(action)
}

fun <INPUT, OUTPUT> single(action: (INPUT) -> Single<out OUTPUT>): SingleWorker<INPUT, OUTPUT> {
    return SingleWorker(action)
}

fun <INPUT> completable(action: (INPUT) -> Completable): CompletableWorker<INPUT> {
    return CompletableWorker(action)
}
