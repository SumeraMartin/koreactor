package com.sumera.koreactor.domain.base

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

abstract class BaseFlowableInteractor<T> {

    abstract fun create(): Flowable<T>

    open fun execute(): Flowable<T> {
        return create()
                .subscribeOn(Schedulers.io())
                .doOnError { e -> Timber.e(e) }
    }
}
