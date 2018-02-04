package com.sumera.koreactorexampleapp

import android.app.Activity
import android.app.Application
import com.sumera.koreactorexampleapp.injection.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasActivityInjector {

	@Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

	override fun onCreate() {
		super.onCreate()

		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		}

		DaggerAppComponent.builder().create(this).inject(this);
	}

	override fun activityInjector() : AndroidInjector<Activity> {
		return activityInjector
	}
}
