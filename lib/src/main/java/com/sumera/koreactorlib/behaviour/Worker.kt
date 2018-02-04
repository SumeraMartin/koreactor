package com.sumera.koreactorlib.behaviour

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
