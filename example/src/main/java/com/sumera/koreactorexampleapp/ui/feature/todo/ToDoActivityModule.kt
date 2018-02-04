package com.sumera.koreactorexampleapp.ui.feature.todo

import android.app.Activity
import com.sumera.koreactorexampleapp.injection.PerActivity
import com.sumera.koreactorexampleapp.injection.base.BaseActivityModule
import com.sumera.koreactorexampleapp.ui.feature.infinity.InfinityActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class ToDoActivityModule {

	@Binds
	@PerActivity
	abstract fun activity(toDoActivity: InfinityActivity): Activity
}