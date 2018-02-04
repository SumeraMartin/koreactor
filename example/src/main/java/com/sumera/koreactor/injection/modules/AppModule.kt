package com.sumera.koreactor.injection.modules

import android.app.Application
import android.content.Context
import com.sumera.koreactor.App
import com.sumera.koreactor.injection.ApplicationContext
import com.sumera.koreactor.injection.PerActivity
import com.sumera.koreactor.injection.PerFragment
import com.sumera.koreactor.ui.feature.counter.CounterActivity
import com.sumera.koreactor.ui.feature.counter.CounterActivityModule
import com.sumera.koreactor.ui.feature.infinity.InfinityActivity
import com.sumera.koreactor.ui.feature.infinity.InfinityActivityModule
import com.sumera.koreactor.ui.feature.main.MainActivity
import com.sumera.koreactor.ui.feature.main.MainActivityModule
import com.sumera.koreactor.ui.feature.simpleloading.SimpleLoadingActivity
import com.sumera.koreactor.ui.feature.simpleloading.SimpleLoadingActivityModule
import com.sumera.koreactor.ui.feature.timer.TimerFragment
import com.sumera.koreactor.ui.feature.timer.TimerFragmentModule
import com.sumera.koreactor.ui.feature.timer.activity.TimerActivity
import com.sumera.koreactor.ui.feature.timer.activity.TimerActivityModule
import com.sumera.koreactor.ui.feature.todo.ToDoActivity
import com.sumera.koreactor.ui.feature.todo.ToDoActivityModule
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
