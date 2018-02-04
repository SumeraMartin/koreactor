package com.sumera.koreactorexampleapp.ui.feature.timer

import android.support.v4.app.Fragment
import com.sumera.koreactorexampleapp.injection.base.BaseFragmentModule
import com.sumera.koreactorexampleapp.injection.PerFragment
import dagger.Binds
import dagger.Module

@Module(includes = [BaseFragmentModule::class])
abstract class TimerFragmentModule {

    @Binds
    @PerFragment
    abstract fun timerFragment(timerFragment: TimerFragment): Fragment
}
