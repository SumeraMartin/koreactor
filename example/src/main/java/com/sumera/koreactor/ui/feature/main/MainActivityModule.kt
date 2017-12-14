package com.sumera.koreactor.ui.feature.main

import android.app.Activity
import com.sumera.koreactor.injection.base.BaseActivityModule
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class MainActivityModule {

	@Binds
	@PerActivity
	abstract fun activity(mainActivity: MainActivity): Activity
}
