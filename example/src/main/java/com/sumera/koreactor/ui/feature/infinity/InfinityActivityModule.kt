package com.sumera.koreactor.ui.feature.infinity

import android.app.Activity
import com.sumera.koreactor.injection.base.BaseActivityModule
import com.sumera.koreactor.ui.feature.todo.ToDoActivity
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class InfinityActivityModule {

	@Binds
	@PerActivity
	abstract fun activity(toDoActivity: ToDoActivity): Activity
}