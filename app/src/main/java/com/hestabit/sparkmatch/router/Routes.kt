package com.hestabit.sparkmatch.router

import android.app.Activity
import android.content.Context
import java.lang.ref.WeakReference

object Routes {

    private var currentRoute: Int? = null
    private var currentContext: WeakReference<Context>? = null
    private var currentActivity: WeakReference<Activity>? = null

    const val SPLASH = "Splash"
    const val SIGN_UP = "Sign Up"
    const val PROFILE = "Profile"
    const val GALLERY = "Gallery"
    const val PHOTO_FULLSCREEN = "Photo fullscreen"
    const val STORIES = "Stories"
    const val ONBOARDING_SCREEN = "Onboarding Screen"
    const val DASHBOARD_SCREEN = "Dashboard Screen"
    const val CHAT_SCREEN = "Chat Screen"
    const val MATCH_FOUND_SCREEN = "Match Found Screen"
    const val EDIT_PROFILE_SCREEN = "Edit Profile Screen"
    const val POP = "Pop"

    fun getNextAuthRoute(currentRoute: String?): String? {
        return when (currentRoute) {
            SPLASH -> AuthRoute.SignUp.route
            AuthRoute.SignUp.route -> AuthRoute.PhoneNumber.route
            AuthRoute.PhoneNumber.route -> AuthRoute.Email.route
            AuthRoute.Email.route -> AuthRoute.Code.route
            AuthRoute.Code.route -> AuthRoute.ProfileDetails.route
            AuthRoute.ProfileDetails.route -> AuthRoute.Gender.route
            AuthRoute.Gender.route -> AuthRoute.InterestPreference.route
            AuthRoute.InterestPreference.route -> AuthRoute.About.route
            AuthRoute.About.route -> AuthRoute.Passions.route
            AuthRoute.Passions.route -> AuthRoute.Friends.route
            AuthRoute.Friends.route -> AuthRoute.Notifications.route
            AuthRoute.Notifications.route -> DASHBOARD_SCREEN
            else -> null
        }
    }

    fun getCurrentContext() = currentContext!!.get()!!

}