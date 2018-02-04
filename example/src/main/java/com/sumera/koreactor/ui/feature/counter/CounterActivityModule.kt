package com.sumera.koreactor.ui.feature.counter

import android.app.Activity
import com.sumera.koreactor.injection.base.BaseActivityModule
import com.sumera.koreactor.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class CounterActivityModule {

	@Binds
	@PerActivity
	abstract fun activity(counterActivity: CounterActivity): Activity
}
