package com.sumera.koreactor.ui.feature.infinity

import android.app.Activity
import com.sumera.koreactor.injection.base.BaseActivityModule
import com.sumera.koreactor.ui.feature.todo.ToDoActivity
import com.sumera.koreactor.injection.PerActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class InfinityActivityModule {

	@Binds
	@PerActivity
	abstract fun activity(toDoActivity: ToDoActivity): Activity
}