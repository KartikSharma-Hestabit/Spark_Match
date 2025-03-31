package com.hestabit.sparkmatch.router

sealed class AuthRoute(val route: String) {
    object SignUp: AuthRoute("SignUp")
    object PhoneNumber: AuthRoute("PhoneNumber")
    object Email: AuthRoute("Email")
    object Password: AuthRoute("Password/{identifier}")
    object CreatePassword: AuthRoute("CreatePassword/{identifier}")
    object Code: AuthRoute("Code/{identifier}")
    object VerifyEmail: AuthRoute("VerifyEmail")
    object ProfileDetails: AuthRoute("ProfileDetails")
    object Gender: AuthRoute("Gender")
    object Passions: AuthRoute("Passions")
    object Friends: AuthRoute("Friends")
    object Notifications: AuthRoute("Notifications")
}