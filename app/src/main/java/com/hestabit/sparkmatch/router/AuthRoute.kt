package com.hestabit.sparkmatch.router

sealed class AuthRoute(val route: String){

    object Splash: AuthRoute("Splash")
    object SignUp: AuthRoute("SignUp")
    object PhoneNumber: AuthRoute("PhoneNumber")
    object Email: AuthRoute("Email")
    object Code: AuthRoute("Code")
    object ProfileDetails: AuthRoute("ProfileDetails")
    object Gender: AuthRoute("Gender")
    object Passions: AuthRoute("Passions")
    object Friends: AuthRoute("Friends")
    object Notifications: AuthRoute("Notifications")

    enum class PassionType(val id: String, val title: String, val icon: Int? = null) {
        TRAVEL("travel", "Travel"),
        MUSIC("music", "Music"),
        DANCING("dancing", "Dancing"),
        EXTREME_SPORTS("extreme_sports", "Extreme Sports"),
        POLITICS("politics", "Politics"),
        PHOTOGRAPHY("photography", "Photography"),
        COOKING("cooking", "Cooking"),
        HIKING("hiking", "Hiking"),
        READING("reading", "Reading"),
        SWIMMING("swimming", "Swimming"),
        ART("art", "Art"),
        DRINKING("drinking", "Drinking"),
        SHOPPING("shopping", "Shopping"),
        RUNNING("running", "Running"),
        TENNIS("tennis", "Tennis"),
        CRICKET("cricket", "Cricket");

        override fun toString(): String {
            return id
        }

        companion object {
            fun fromId(id: String): PassionType? {
                return entries.find { it.id == id }
            }
        }
    }
}