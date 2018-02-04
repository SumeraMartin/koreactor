package com.sumera.koreactor.ui.feature.simpleloading

import android.app.Activity
import com.sumera.koreactor.injection.base.BaseActivityModule
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class SimpleLoadingActivityModule {

    @Binds
    @PerActivity
    abstract fun activity(simpleLoadingActivity: SimpleLoadingActivity): Activity
}
