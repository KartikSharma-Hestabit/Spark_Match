package com.hestabit.sparkmatch.routing

import android.app.Activity
import android.content.Context
import java.lang.ref.WeakReference

object Routes {

    private var currentRoute: Int? = null
    private var currentContext: WeakReference<Context>? = null
    private var currentActivity: WeakReference<Activity>? = null

    const val SPLASH_SCREEN = "Splash Screen"
    const val AUTH_SCREEN = "Auth Screen"
    const val ONBOARDING_SCREEN = "Onboarding Screen"
    const val DISCOVER_SCREEN = "Discover Screen"

    fun getCurrentContext() = currentContext!!.get()!!

}