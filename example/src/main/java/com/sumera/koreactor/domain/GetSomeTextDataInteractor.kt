package com.sumera.koreactor.domain

import cz.muni.fi.pv256.movio2.uco_461464.domain.base.BaseObservableInteractor
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class GetSomeTextDataInteractor @Inject constructor() : BaseObservableInteractor<String>() {

	override fun create(): Observable<String> {
		return Observable.just("Some string message")
				.delay(3, TimeUnit.SECONDS)
	}
}