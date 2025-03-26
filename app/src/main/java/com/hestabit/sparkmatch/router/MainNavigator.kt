package com.hestabit.sparkmatch.router

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hestabit.sparkmatch.Utils.printDebug
import com.hestabit.sparkmatch.screens.auth.Code
import com.hestabit.sparkmatch.screens.auth.Email
import com.hestabit.sparkmatch.screens.auth.Friends
import com.hestabit.sparkmatch.screens.auth.Gender
import com.hestabit.sparkmatch.screens.auth.Notifications
import com.hestabit.sparkmatch.screens.auth.Passions
import com.hestabit.sparkmatch.screens.auth.PhoneNumber
import com.hestabit.sparkmatch.screens.auth.ProfileDetails
import com.hestabit.sparkmatch.screens.auth.SignUp
import com.hestabit.sparkmatch.screens.dashboard.DashboardScreen
import com.hestabit.sparkmatch.screens.onboard.OnboardingScreen
import com.hestabit.sparkmatch.screens.profile.Gallery
import com.hestabit.sparkmatch.screens.profile.PhotoFullscreen
import com.hestabit.sparkmatch.screens.profile.Profile
import com.hestabit.sparkmatch.screens.profile.Stories

object MainNavigator {

    @Composable
    fun InitMainNavigator(
        modifier: Modifier = Modifier,
        startRoute: String = Routes.SIGN_UP,
        extraArgs: String = "",
    ) {
        val mainNavController = rememberNavController()
        NavHost(navController = mainNavController, modifier = modifier, startDestination = startRoute){

            composable(route = Routes.ONBOARDING_SCREEN){
                OnboardingScreen { route ->
                    mainNavController.navigate(route) {
                        launchSingleTop = true
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            composable(route = Routes.SIGN_UP){
                SignUp(mainNavController)
            }

            composable(route = Routes.EMAIL){
                Email(mainNavController)
            }

            composable(route = Routes.PHONE_NUMBER){
                PhoneNumber(mainNavController)
            }

            composable(route = Routes.CODE){
                Code(mainNavController)
            }

            composable(route = Routes.PROFILE_DETAILS){
                ProfileDetails(mainNavController)
            }

            composable(route = Routes.GENDER){
                Gender(mainNavController)
            }

            composable(route = Routes.PASSIONS){
                Passions(mainNavController)
            }

            composable(route = Routes.FRIENDS){
                Friends(mainNavController)
            }

            composable(route = Routes.NOTIFICATIONS){
                Notifications(mainNavController)
            }

            composable(route = Routes.DASHBOARD_SCREEN){
                DashboardScreen{
                        route, _ ->
                    mainNavController.navigate(route)
                }
            }

            composable(route = Routes.PROFILE) {
                Profile(mainNavController)
            }
            composable(route = Routes.PHOTO_FULLSCREEN){
                PhotoFullscreen(mainNavController)
            }

            composable(route = Routes.GALLERY){
                Gallery(mainNavController)
            }

            composable(route = Routes.STORIES){
                Stories(mainNavController)
            }
        }
    }
}