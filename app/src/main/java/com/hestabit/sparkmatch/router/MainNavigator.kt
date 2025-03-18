package com.hestabit.sparkmatch.router

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hestabit.sparkmatch.common.BirthdayPicker
import com.hestabit.sparkmatch.screens.auth.Code
import com.hestabit.sparkmatch.screens.auth.Friends
import com.hestabit.sparkmatch.screens.auth.Gender
import com.hestabit.sparkmatch.screens.auth.Notifications
import com.hestabit.sparkmatch.screens.auth.Passions
import com.hestabit.sparkmatch.screens.auth.PhoneNumber
import com.hestabit.sparkmatch.screens.auth.ProfileDetails
import com.hestabit.sparkmatch.screens.auth.SignUp
import com.hestabit.sparkmatch.screens.onboarding.OnboardingScreen

object MainNavigator {

    @Composable
    fun InitMainNavigator(
        modifier: Modifier = Modifier,
        startRoute: String = Routes.SPLASH_SCREEN,
        extraArgs: String = "",
    ) {
        val mainNavController = rememberNavController()
        NavHost(navController = mainNavController, modifier = modifier, startDestination = startRoute){

            composable(route = Routes.SPLASH_SCREEN){

            }

            composable(route = Routes.ONBOARDING_SCREEN){
                OnboardingScreen()
            }

            composable(route = Routes.AUTH_SCREEN){
                SignUp()
            }

            composable(route = Routes.PHONE_NUMBER){
                PhoneNumber()
            }

            composable(route = Routes.CODE){
                Code()
            }

            composable(route = Routes.PROFILE_DETAILS){
                ProfileDetails()
            }

            composable(route = Routes.CALENDAR){
                BirthdayPicker(onSave = {})
            }

            composable(route = Routes.GENDER){
                Gender()
            }

            composable(route = Routes.PASSIONS){
                Passions()
            }

            composable(route = Routes.FRIENDS){
                Friends()
            }

            composable(route = Routes.NOTIFICATIONS){
                Notifications()
            }
        }
    }
}