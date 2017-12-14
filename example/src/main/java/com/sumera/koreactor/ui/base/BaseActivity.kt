package com.sumera.koreactor.ui.base

import android.app.Activity
import android.os.Bundle
import com.sumera.koreactor.lib.reactor.data.MviState
import com.sumera.koreactor.lib.view.implementation.MviAppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

abstract class BaseActivity<STATE> : MviAppCompatActivity<STATE>(), HasActivityInjector
		where STATE : MviState {

	@Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

	abstract protected val layoutRes: Int

	override fun onCreate(savedInstanceState: Bundle?) {
		AndroidInjection.inject(this);

		super.onCreate(savedInstanceState)

		setContentView(layoutRes)
	}

	override fun activityInjector(): AndroidInjector<Activity> {
		return activityInjector
	}
}
