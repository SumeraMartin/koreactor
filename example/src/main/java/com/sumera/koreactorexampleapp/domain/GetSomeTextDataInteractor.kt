package com.sumera.koreactorexampleapp.domain

import com.sumera.koreactorexampleapp.domain.base.BaseSingleInteractor
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class GetSomeTextDataInteractor @Inject constructor() : BaseSingleInteractor<String>() {

	override fun create(): Single<String> {
		return Single.just("Some string message")
				.delay(3, TimeUnit.SECONDS)
	}
}