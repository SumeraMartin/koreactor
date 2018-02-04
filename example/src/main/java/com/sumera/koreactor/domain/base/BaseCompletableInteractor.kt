package com.sumera.koreactor.domain.base

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

abstract class BaseCompletableInteractor {

    abstract fun create(): Completable

    open fun execute(): Completable {
        return create()
                .subscribeOn(Schedulers.io())
                .doOnError { e -> Timber.e(e) }
    }
}