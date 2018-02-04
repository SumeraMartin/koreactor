package com.sumera.koreactorexampleapp.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.sumera.koreactor.view.implementation.MviAppCompatActivityDelegate
import com.sumera.koreactor.reactor.data.MviState
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity<STATE> : MviAppCompatActivityDelegate<STATE>(), HasSupportFragmentInjector
		where STATE : MviState {

	@Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

	abstract protected val layoutRes: Int

	override fun onCreate(savedInstanceState: Bundle?) {
		AndroidInjection.inject(this)

		super.onCreate(savedInstanceState)

		if (layoutRes != 0) {
			setContentView(layoutRes)
		}
	}

	override fun supportFragmentInjector(): AndroidInjector<Fragment> {
		return fragmentInjector
	}
}
