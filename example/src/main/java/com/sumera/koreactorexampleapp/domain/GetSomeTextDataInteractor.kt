package com.sumera.koreactorexampleapp.domain

import com.sumera.koreactorexampleapp.domain.base.BaseObservableInteractor
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class GetSomeTextDataInteractor @Inject constructor() : BaseObservableInteractor<String>() {

	override fun create(): Observable<String> {
		return Observable.just("Some string message")
				.delay(3, TimeUnit.SECONDS)
	}
}