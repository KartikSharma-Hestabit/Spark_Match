package com.hestabit.sparkmatch.router

import android.app.Activity
import android.content.Context
import java.lang.ref.WeakReference

object Routes {

    private var currentRoute: Int? = null
    private var currentContext: WeakReference<Context>? = null
    private var currentActivity: WeakReference<Activity>? = null

    const val SPLASH_SCREEN = "Splash Screen"
    const val AUTH_SCREEN = "Auth Screen"
    const val PHONE_NUMBER = "Phone Number"
    const val CODE = "Code"
    const val PROFILE_DETAILS = "Profile Details"
    const val CALENDAR = "Calendar"
    const val GENDER = "Gender"
    const val PASSIONS = "Passions"
    const val FRIENDS = "Friends"
    const val NOTIFICATIONS = "Notifications"
    const val ONBOARDING_SCREEN = "Onboarding Screen"
    const val DISCOVER_SCREEN = "Discover Screen"

    fun getCurrentContext() = Routes.currentContext!!.get()!!

}