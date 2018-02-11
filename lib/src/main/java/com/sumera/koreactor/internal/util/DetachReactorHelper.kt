package com.sumera.koreactor.internal.util

import android.app.Activity
import android.support.v4.app.Fragment

object DetachReactorHelper {

	fun shouldDetachReactor(activity: Activity): Boolean {
		return activity.isFinishing
	}

	fun shouldDetachReactor(fragment: Fragment): Boolean {
		val activity = fragment.activity ?: throw IllegalStateException("Fragment is not attached to activity " + fragment)

		if (activity.isChangingConfigurations || activity.isFinishing) {
			return false
		}
		return fragment.isRemoving
	}
}