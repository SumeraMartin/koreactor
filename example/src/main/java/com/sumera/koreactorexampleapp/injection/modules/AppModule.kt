package com.sumera.koreactorexampleapp.injection.modules

import android.app.Application
import android.content.Context
import com.sumera.koreactorexampleapp.App
import com.sumera.koreactorexampleapp.injection.ApplicationContext
import com.sumera.koreactorexampleapp.injection.PerActivity
import com.sumera.koreactorexampleapp.injection.PerFragment
import com.sumera.koreactorexampleapp.ui.feature.counter.CounterActivity
import com.sumera.koreactorexampleapp.ui.feature.counter.CounterActivityModule
import com.sumera.koreactorexampleapp.ui.feature.infinity.InfinityActivity
import com.sumera.koreactorexampleapp.ui.feature.infinity.InfinityActivityModule
import com.sumera.koreactorexampleapp.ui.feature.main.MainActivity
import com.sumera.koreactorexampleapp.ui.feature.main.MainActivityModule
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.SimpleLoadingActivity
import com.sumera.koreactorexampleapp.ui.feature.simpleloading.SimpleLoadingActivityModule
import com.sumera.koreactorexampleapp.ui.feature.timer.TimerFragment
import com.sumera.koreactorexampleapp.ui.feature.timer.TimerFragmentModule
import com.sumera.koreactorexampleapp.ui.feature.timer.activity.TimerActivity
import com.sumera.koreactorexampleapp.ui.feature.timer.activity.TimerActivityModule
import com.sumera.koreactorexampleapp.ui.feature.todo.ToDoActivity
import com.sumera.koreactorexampleapp.ui.feature.todo.ToDoActivityModule
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module(includes = [(AndroidInjectionModule::class), (NetworkModule::class)])
abstract class  AppModule {

    @Binds
    @Singleton
    @ApplicationContext
    abstract fun applicationContext(app: App): Context

    @Binds
    @Singleton
    abstract fun application(app: App): Application

    @PerActivity
    @ContributesAndroidInjector(modules = [(InfinityActivityModule::class)])
    abstract fun infinityActivityInjector(): InfinityActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [(ToDoActivityModule::class)])
    abstract fun todoActivityInjector(): ToDoActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(CounterActivityModule::class))
    abstract fun counterActivityInjector(): CounterActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
    abstract fun mainActivityInjector(): MainActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(SimpleLoadingActivityModule::class))
    abstract fun simpleLoadingActivityInjector(): SimpleLoadingActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(TimerActivityModule::class))
    abstract fun timerActivityInjector(): TimerActivity

    @PerFragment
    @ContributesAndroidInjector(modules = [TimerFragmentModule::class])
    abstract fun timerFragmentInjector(): TimerFragment
}
