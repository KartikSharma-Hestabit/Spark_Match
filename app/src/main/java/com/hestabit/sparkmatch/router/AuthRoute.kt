package com.hestabit.sparkmatch.router

sealed class AuthRoute(val route: String) {
    object SignUp: AuthRoute("SignUp")
    object PhoneNumber: AuthRoute("PhoneNumber")
    object Email: AuthRoute("Email")
    object Password: AuthRoute("Password/{identifier}") // New route for password screen
    object Code: AuthRoute("Code/{identifier}") // Updated route for verification code
    object ProfileDetails: AuthRoute("ProfileDetails")
    object Gender: AuthRoute("Gender")
    object Passions: AuthRoute("Passions")
    object Friends: AuthRoute("Friends")
    object Notifications: AuthRoute("Notifications")
}