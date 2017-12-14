package com.sumera.koreactor.injection.modules

import android.app.Application
import android.content.Context
import com.sumera.koreactor.App
import com.sumera.koreactor.injection.ApplicationContext
import com.sumera.koreactor.ui.feature.counter.CounterActivity
import com.sumera.koreactor.ui.feature.counter.CounterActivityModule
import com.sumera.koreactor.ui.feature.infinity.InfinityActivityModule
import com.sumera.koreactor.ui.feature.main.MainActivity
import com.sumera.koreactor.ui.feature.main.MainActivityModule
import com.sumera.koreactor.ui.feature.todo.InfinityActivity
import com.sumera.koreactor.ui.feature.todo.ToDoActivity
import com.sumera.koreactor.ui.feature.todo.ToDoActivityModule
import cz.muni.fi.pv256.movio2.uco_461464.injection.PerActivity
import cz.muni.fi.pv256.movio2.uco_461464.injection.modules.NetworkModule
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module(includes = arrayOf(AndroidInjectionModule::class, NetworkModule::class))
abstract class AppModule {

    @Binds
    @Singleton
    @ApplicationContext
    abstract fun applicationContext(app: App): Context

    @Binds
    @Singleton
    abstract fun application(app: App): Application

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(InfinityActivityModule::class))
    abstract fun infinityActivityInjector(): InfinityActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(ToDoActivityModule::class))
    abstract fun todoActivityInjector(): ToDoActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(CounterActivityModule::class))
    abstract fun counterActivityInjector(): CounterActivity

    @PerActivity
    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
    abstract fun mainActivityInjector(): MainActivity

}
