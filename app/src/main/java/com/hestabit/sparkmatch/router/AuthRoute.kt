package com.hestabit.sparkmatch.router

import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.data.PassionList

sealed class AuthRoute(val route: String){

    object Splash: AuthRoute("Splash")
    object SignUp: AuthRoute("SignUp")
    object PhoneNumber: AuthRoute("PhoneNumber")
    object Email: AuthRoute("Email")
    object Code: AuthRoute("Code")
    object ProfileDetails: AuthRoute("ProfileDetails")
    object Gender: AuthRoute("Gender")
    object InterestPreference: AuthRoute("InterestPreference")
    object Passions: AuthRoute("Passions")
    object Friends: AuthRoute("Friends")
    object Notifications: AuthRoute("Notifications")

    enum class PassionType(val id: String, val title: String, val iconRes: Int) {
        PHOTOGRAPHY("photography", "Photography", R.drawable.photography),
        SHOPPING("shopping", "Shopping", R.drawable.weixin_market),
        KARAOKE("karaoke", "Karaoke", R.drawable.voice),
        YOGA("yoga", "Yoga", R.drawable.viencharts),
        COOKING("cooking", "Cooking", R.drawable.noodles),
        TENNIS("tennis", "Tennis", R.drawable.tennis),
        RUNNING("running", "Running", R.drawable.sport),
        SWIMMING("swimming", "Swimming", R.drawable.ripple),
        ART("art", "Art", R.drawable.platte),
        TRAVELING("traveling", "Traveling", R.drawable.outdoor),
        EXTREME("extreme", "Extreme", R.drawable.parachute),
        MUSIC("music", "Music", R.drawable.music),
        DRINKING("drinking", "Drink", R.drawable.goblet_full),
        VIDEO_GAMES("video_games", "Video games", R.drawable.game_handle);

        override fun toString(): String {
            return id
        }

        companion object {
            fun fromId(id: String): PassionType? {
                return entries.find { it.id == id }
            }

            fun fromTitle(title: String): PassionType? {
                return entries.find { it.title.equals(title, ignoreCase = true) }
            }

            // Convert to PassionList for UI
            fun toPassionLists(): List<PassionList> {
                return entries.map {
                    PassionList(it.title, it.iconRes, false)
                }
            }
        }
    }
}