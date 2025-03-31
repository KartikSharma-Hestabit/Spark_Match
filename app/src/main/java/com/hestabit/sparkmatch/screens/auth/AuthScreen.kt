package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.screens.dashboard.DashboardScreen
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.modernist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen() {
    val authNavController = rememberNavController()
    val currentRoute = authNavController.currentBackStackEntryAsState().value?.destination?.route
    val nextRoute = getNextAuthRoute(currentRoute)

    Scaffold (
        topBar = {
            if (currentRoute != AuthRoute.SignUp.route && currentRoute != Routes.DASHBOARD_SCREEN) {
                TopAppBar(
                    title = { /* You can add a title if needed */ },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    navigationIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            BackButton(authNavController, HotPink)
                            Spacer(modifier = Modifier.weight(1f))

                            // Skip button (optional for some screens)
                            if (shouldShowSkipButton(currentRoute)) {
                                TextButton(onClick = {
                                    nextRoute?.let {
                                        authNavController.navigate(it)
                                    }
                                }) {
                                    Text(
                                        text = "Skip",
                                        textAlign = TextAlign.Center,
                                        fontFamily = modernist,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = HotPink
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    ) { paddingValues ->
        NavHost(navController = authNavController, startDestination = AuthRoute.SignUp.route) {
            // SignUp screen
            composable(route = AuthRoute.SignUp.route) {
                SignUp(authNavController, paddingValues)
            }

            // Phone number screen
            composable(route = AuthRoute.PhoneNumber.route) {
                PhoneNumber(authNavController, paddingValues)
            }

            // Email screen
            composable(route = AuthRoute.Email.route) {
                Email(authNavController, paddingValues)
            }

            // Password screen for existing users
            composable(
                route = AuthRoute.Password.route,
                arguments = listOf(
                    navArgument("identifier") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val identifier = backStackEntry.arguments?.getString("identifier") ?: ""
                PasswordScreen(authNavController, paddingValues, identifier)
            }

            // Create password screen for new users
            composable(
                route = AuthRoute.CreatePassword.route,
                arguments = listOf(
                    navArgument("identifier") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val identifier = backStackEntry.arguments?.getString("identifier") ?: ""
                CreatePasswordScreen(authNavController, paddingValues, identifier)
            }

            // Code verification screen
            composable(
                route = AuthRoute.Code.route,
                arguments = listOf(
                    navArgument("identifier") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val identifier = backStackEntry.arguments?.getString("identifier") ?: ""
                Code(authNavController, paddingValues, identifier)
            }

            // Email verification screen
            composable(route = AuthRoute.VerifyEmail.route) {
                VerifyEmail(authNavController, paddingValues)
            }

            // Profile details screen
            composable(route = AuthRoute.ProfileDetails.route) {
                ProfileDetails(authNavController, paddingValues)
            }

            // Gender selection screen
            composable(route = AuthRoute.Gender.route) {
                Gender(authNavController, paddingValues)
            }

            // Passions/interests screen
            composable(route = AuthRoute.Passions.route) {
                Passions(authNavController, paddingValues)
            }

            // Friends screen
            composable(route = AuthRoute.Friends.route) {
                Friends(authNavController, paddingValues)
            }

            // Notification screen
            composable(route = AuthRoute.Notifications.route) {
                Notifications(authNavController, paddingValues)
            }

            // Dashboard screen
            composable(route = Routes.DASHBOARD_SCREEN) {
                DashboardScreen { route, _ ->
                    authNavController.navigate(route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}

// Helper function to determine if we should show the skip button
fun shouldShowSkipButton(currentRoute: String?): Boolean {
    return when {
        currentRoute?.contains(AuthRoute.Password.route) == true -> false
        currentRoute?.contains(AuthRoute.CreatePassword.route) == true -> false
        currentRoute?.contains(AuthRoute.Code.route) == true -> false
        currentRoute == AuthRoute.VerifyEmail.route -> false
        else -> true
    }
}

fun getNextAuthRoute(currentRoute: String?): String? {
    return when {
        currentRoute == AuthRoute.SignUp.route -> AuthRoute.PhoneNumber.route
        currentRoute == AuthRoute.PhoneNumber.route -> AuthRoute.Email.route
        currentRoute == AuthRoute.Email.route -> AuthRoute.ProfileDetails.route
        currentRoute == AuthRoute.ProfileDetails.route -> AuthRoute.Gender.route
        currentRoute == AuthRoute.Gender.route -> AuthRoute.Passions.route
        currentRoute == AuthRoute.Passions.route -> AuthRoute.Friends.route
        currentRoute == AuthRoute.Friends.route -> AuthRoute.Notifications.route
        currentRoute == AuthRoute.Notifications.route -> Routes.DASHBOARD_SCREEN
        else -> null
    }
}