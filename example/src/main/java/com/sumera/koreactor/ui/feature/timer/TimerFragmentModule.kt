package com.sumera.koreactor.ui.feature.timer

import android.support.v4.app.Fragment
import cz.muni.fi.pv256.movio2.uco_461464.injection.BaseFragmentModule
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerFragment
import dagger.Binds
import dagger.Module

@Module(includes = [BaseFragmentModule::class])
abstract class TimerFragmentModule {

    @Binds
    @PerFragment
    abstract fun timerFragment(timerFragment: TimerFragment): Fragment
}
