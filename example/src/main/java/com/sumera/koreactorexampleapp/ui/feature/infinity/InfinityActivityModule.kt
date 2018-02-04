package com.sumera.koreactorexampleapp.ui.feature.infinity

import android.app.Activity
import com.sumera.koreactorexampleapp.injection.base.BaseActivityModule
import com.sumera.koreactorexampleapp.ui.feature.todo.ToDoActivity
import com.sumera.koreactorexampleapp.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class InfinityActivityModule {

	@Binds
	@PerActivity
	abstract fun activity(toDoActivity: ToDoActivity): Activity
}