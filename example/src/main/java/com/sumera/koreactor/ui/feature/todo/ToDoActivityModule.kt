package com.sumera.koreactor.ui.feature.todo

import android.app.Activity
import com.sumera.koreactor.injection.base.BaseActivityModule
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class ToDoActivityModule {

	@Binds
	@PerActivity
	abstract fun activity(toDoActivity: InfinityActivity): Activity
}