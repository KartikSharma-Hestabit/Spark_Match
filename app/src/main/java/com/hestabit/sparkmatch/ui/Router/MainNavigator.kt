package com.hestabit.sparkmatch.ui.Router

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hestabit.sparkmatch.ui.screens.Onboarding.OnboardingScreen
import com.hestabit.sparkmatch.utils.getString

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

        }

    }

}