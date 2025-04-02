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

}