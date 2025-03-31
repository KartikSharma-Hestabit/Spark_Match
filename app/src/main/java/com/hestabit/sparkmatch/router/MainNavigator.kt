package com.hestabit.sparkmatch.router

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hestabit.sparkmatch.common.Test
import com.hestabit.sparkmatch.screens.chat.MessageScreen
import com.hestabit.sparkmatch.screens.dashboard.DashboardScreen
import com.hestabit.sparkmatch.screens.discover.MatchFoundScreen
import com.hestabit.sparkmatch.screens.onboard.OnboardingScreen
import com.hestabit.sparkmatch.screens.profile.Gallery
import com.hestabit.sparkmatch.screens.profile.PhotoFullscreen
import com.hestabit.sparkmatch.screens.profile.Profile
import com.hestabit.sparkmatch.screens.profile.Stories

object MainNavigator {

    @RequiresApi(Build.VERSION_CODES.S)
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

            composable(route = Routes.DASHBOARD_SCREEN){
                DashboardScreen{
                        route, _ ->
                    mainNavController.navigate(route)
                }
            }

            composable(route = Routes.MATCH_FOUND_SCREEN){
                MatchFoundScreen{ route ->
                    if(route == Routes.POP){
                        mainNavController.popBackStack()
                    }
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

            composable(route = Routes.TEST){
                Test(mainNavController)
            }

            composable(route = Routes.CHAT_SCREEN){
                MessageScreen(onNavigate = {})
            }
        }
    }
}