package com.hestabit.sparkmatch.router

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hestabit.sparkmatch.common.Splash
import com.hestabit.sparkmatch.screens.auth.AuthScreen
import com.hestabit.sparkmatch.screens.auth.Code
import com.hestabit.sparkmatch.screens.auth.Email
import com.hestabit.sparkmatch.screens.auth.Friends
import com.hestabit.sparkmatch.screens.auth.Gender
import com.hestabit.sparkmatch.screens.auth.Notifications
import com.hestabit.sparkmatch.screens.auth.Passions
import com.hestabit.sparkmatch.screens.auth.PhoneNumber
import com.hestabit.sparkmatch.screens.auth.ProfileDetails
import com.hestabit.sparkmatch.screens.auth.SignUp
import com.hestabit.sparkmatch.screens.chat.MessageScreen
import com.hestabit.sparkmatch.screens.dashboard.DashboardScreen
import com.hestabit.sparkmatch.screens.discover.MatchFoundScreen
import com.hestabit.sparkmatch.screens.onboard.OnboardingScreen
import com.hestabit.sparkmatch.screens.profile.EditProfileScreen
import com.hestabit.sparkmatch.screens.profile.Gallery
import com.hestabit.sparkmatch.screens.profile.PhotoFullscreen
import com.hestabit.sparkmatch.screens.profile.Profile
import com.hestabit.sparkmatch.screens.profile.Stories
import com.hestabit.sparkmatch.viewmodel.AuthViewModel
import javax.inject.Inject

object MainNavigator {

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    fun InitMainNavigator(
        modifier: Modifier = Modifier,
        startRoute: String = Routes.ONBOARDING_SCREEN,
        extraArgs: String = "",
    ) {
        val mainNavController = rememberNavController()
        val authViewModel: AuthViewModel = hiltViewModel()
        NavHost(
            navController = mainNavController,
            modifier = modifier,
            startDestination = startRoute
        ) {

            composable(route = Routes.SPLASH) {
                Splash(authViewModel = authViewModel) { route ->
                    mainNavController.navigate(route)
                }
            }

            composable(route = Routes.ONBOARDING_SCREEN) {
                OnboardingScreen(authViewModel = authViewModel) { route ->
                    mainNavController.navigate(route) {
                        launchSingleTop = true
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            composable(route = Routes.SIGN_UP) {
                AuthScreen(authViewModel = authViewModel) { route ->
                    mainNavController.navigate(route) {
                        launchSingleTop = true
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            composable(route = Routes.DASHBOARD_SCREEN) {
                DashboardScreen { route, _ ->
                    mainNavController.navigate(route)
                }
            }

            composable(route = Routes.MATCH_FOUND_SCREEN) {
                MatchFoundScreen { route ->
                    if (route == Routes.POP) {
                        mainNavController.popBackStack()
                    }
                }
            }

            composable(route = Routes.PROFILE) {
                Profile(mainNavController)
            }
            composable(route = Routes.PHOTO_FULLSCREEN) {
                PhotoFullscreen(mainNavController)
            }

            composable(route = Routes.GALLERY) {
                Gallery(mainNavController)
            }

            composable(route = Routes.STORIES) {
                Stories(mainNavController)
            }

            composable(route = Routes.CHAT_SCREEN) {
                MessageScreen(onNavigate = {})
            }

            composable(route = Routes.EDIT_PROFILE_SCREEN) {
                EditProfileScreen { route ->
                    if (route == Routes.POP) {
                        mainNavController.popBackStack()
                    } else {
                        mainNavController.navigate(route)
                    }

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @Composable
    fun InitAuthNavigator(
        modifier: Modifier = Modifier,
        authNavController: NavHostController,
        authViewModel: AuthViewModel,
        onNavigate: (String) -> Unit
    ) {
        NavHost(
            navController = authNavController,
            startDestination = AuthRoute.SignUp.route,
            modifier = modifier
        ) {

            composable(route = AuthRoute.SignUp.route) {
                SignUp(authViewModel = authViewModel) { route ->
                    authNavController.navigate(route)
                }
            }

            composable(route = AuthRoute.PhoneNumber.route) {
                PhoneNumber(
                    authViewModel = authViewModel
                ){ route ->
                    authNavController.navigate(route)
                }
            }

            composable(route = AuthRoute.Email.route) {
                Email(authViewModel = authViewModel) { route ->
                    if (route == Routes.DASHBOARD_SCREEN){
                        onNavigate(route)
                    } else {
                        authNavController.navigate(route)
                    }
                }
            }

            composable(route = AuthRoute.Code.route) {
                Code (
                    authViewModel = authViewModel
                ){ route ->
                    authNavController.navigate(route)
                }
            }

            composable(route = AuthRoute.ProfileDetails.route) {
                ProfileDetails { route ->
                    authNavController.navigate(route)
                }
            }

            composable(route = AuthRoute.Gender.route) {
                Gender { route ->
                    authNavController.navigate(route)
                }
            }

            composable(route = AuthRoute.Passions.route) {
                Passions { route ->
                    authNavController.navigate(route)
                }
            }

            composable(route = AuthRoute.Friends.route) {
                Friends { route ->
                    authNavController.navigate(route)
                }
            }

            composable(route = AuthRoute.Notifications.route) {
                Notifications { route ->
                    onNavigate(route)
                }
            }
        }
    }
}