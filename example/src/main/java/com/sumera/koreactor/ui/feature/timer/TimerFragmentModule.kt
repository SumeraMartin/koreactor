package com.sumera.koreactor.ui.feature.timer

import android.support.v4.app.Fragment
import com.sumera.koreactor.injection.base.BaseFragmentModule
import com.sumera.koreactor.injection.PerFragment
import dagger.Binds
import dagger.Module

@Module(includes = [BaseFragmentModule::class])
abstract class TimerFragmentModule {

    @Binds
    @PerFragment
    abstract fun timerFragment(timerFragment: TimerFragment): Fragment
}
