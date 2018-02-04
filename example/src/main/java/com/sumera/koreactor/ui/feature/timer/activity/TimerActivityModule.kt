package com.sumera.koreactor.ui.feature.timer.activity

import com.sumera.koreactor.injection.base.BaseActivityModule
import com.sumera.koreactor.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class TimerActivityModule {

    @Binds
    @PerActivity
    abstract fun activity(timerActivity: TimerActivity): TimerActivity
}
