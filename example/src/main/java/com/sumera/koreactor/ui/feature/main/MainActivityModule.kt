package com.sumera.koreactor.ui.feature.main

import android.app.Activity
import com.sumera.koreactor.injection.base.BaseActivityModule
import com.sumera.koreactor.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class MainActivityModule {

	@Binds
	@PerActivity
	abstract fun activity(mainActivity: MainActivity): Activity
}
