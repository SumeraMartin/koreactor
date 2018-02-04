package com.sumera.koreactor.domain.base

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

abstract class BaseSingleInteractor<T> {

    abstract fun create(): Single<T>

    open fun execute(): Single<T> {
        return create()
                .subscribeOn(Schedulers.io())
                .doOnError { e -> Timber.e(e) }
    }
}