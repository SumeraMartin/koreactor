package com.sumera.koreactorexampleapp.injection

import com.sumera.koreactorexampleapp.App
import com.sumera.koreactorexampleapp.injection.modules.AppModule
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
internal interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
