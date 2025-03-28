package com.hestabit.sparkmatch.router

import android.app.Activity
import android.content.Context
import java.lang.ref.WeakReference

object Routes {

    private var currentRoute: Int? = null
    private var currentContext: WeakReference<Context>? = null
    private var currentActivity: WeakReference<Activity>? = null

    const val TEST = "Test"
    const val SIGN_UP = "Sign Up"
    const val PROFILE = "Profile"
    const val GALLERY = "Gallery"
    const val PHOTO_FULLSCREEN = "Photo fullscreen"
    const val STORIES = "Stories"
    const val ONBOARDING_SCREEN = "Onboarding Screen"
    const val DASHBOARD_SCREEN = "Dashboard Screen"
    const val CHAT_SCREEN = "Chat Screen"
    const val MATCH_FOUND_SCREEN = "Match Found Screen"
    const val POP = "Pop"

    fun getCurrentContext() = currentContext!!.get()!!

}