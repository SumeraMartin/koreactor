package com.sumera.koreactor.ui.feature.todo

import android.app.Activity
import com.sumera.koreactor.injection.PerActivity
import com.sumera.koreactor.injection.base.BaseActivityModule
import com.sumera.koreactor.ui.feature.infinity.InfinityActivity
import dagger.Binds
import dagger.Module

@Module(includes = [BaseActivityModule::class])
abstract class ToDoActivityModule {

	@Binds
	@PerActivity
	abstract fun activity(toDoActivity: InfinityActivity): Activity
}