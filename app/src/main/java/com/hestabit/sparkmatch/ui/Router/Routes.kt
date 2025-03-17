package com.hestabit.sparkmatch.ui.Router

import android.app.Activity
import android.content.Context
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.utils.getString
import java.lang.ref.WeakReference

object Routes {

    private var currentRoute: Int? = null
    private var currentContext: WeakReference<Context>? = null
    private var currentActivity: WeakReference<Activity>? = null

    const val SPLASH_SCREEN = "Splash Screen"
    const val AUTH_SCREEN = "Auth Screen"
    const val ONBOARDING_SCREEN = "Onboarding Screen"
    const val DASHBOARD_SCREEN = "Dashboard Screen"

    fun getCurrentContext() = currentContext!!.get()!!

}