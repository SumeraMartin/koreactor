package com.sumera.koreactorexampleapp.ui.feature.counter

import android.app.Activity
import com.sumera.koreactorexampleapp.injection.base.BaseActivityModule
import com.sumera.koreactorexampleapp.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class CounterActivityModule {

	@Binds
	@PerActivity
	abstract fun activity(counterActivity: CounterActivity): Activity
}
