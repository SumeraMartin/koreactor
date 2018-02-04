package com.sumera.koreactorexampleapp.ui.feature.timer.activity

import com.sumera.koreactorexampleapp.injection.base.BaseActivityModule
import com.sumera.koreactorexampleapp.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class TimerActivityModule {

    @Binds
    @PerActivity
    abstract fun activity(timerActivity: TimerActivity): TimerActivity
}
