package com.sumera.koreactorexampleapp.ui.feature.toasts

import com.sumera.koreactorexampleapp.injection.PerActivity
import com.sumera.koreactorexampleapp.injection.base.BaseActivityModule
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class ToastsActivityModule {

    @Binds
    @PerActivity
    abstract fun activity(toastsActivity: ToastsActivity): ToastsActivity
}
