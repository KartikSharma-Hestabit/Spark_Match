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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.screens.dashboard.DashboardScreen
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.modernist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(){

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
                            TextButton(onClick = { authNavController.navigate(nextRoute.toString()) }) {
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
                    },
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    ){ paddingValues ->
        NavHost(navController = authNavController, startDestination = AuthRoute.SignUp.route){

            composable(route = AuthRoute.SignUp.route){
                SignUp(authNavController, paddingValues)
            }

            composable(route = AuthRoute.PhoneNumber.route){
                PhoneNumber(authNavController, paddingValues)
            }

            composable(route = AuthRoute.Email.route){
                Email(authNavController, paddingValues)
            }

            composable(route = AuthRoute.Code.route){
                Code(authNavController, paddingValues)
            }

            composable(route = AuthRoute.ProfileDetails.route){
                ProfileDetails(authNavController, paddingValues)
            }

            composable(route = AuthRoute.Gender.route){
                Gender(authNavController, paddingValues)
            }

            composable(route = AuthRoute.Passions.route){
                Passions(authNavController, paddingValues)
            }

            composable(route = AuthRoute.Friends.route){
                Friends(authNavController, paddingValues)
            }

            composable(route = AuthRoute.Notifications.route){
                Notifications(authNavController, paddingValues)
            }

            composable(route = Routes.DASHBOARD_SCREEN){
                DashboardScreen{
                        route, _ ->
                    authNavController.navigate(Routes.DASHBOARD_SCREEN) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}

fun getNextAuthRoute(currentRoute: String?): String? {
    return when (currentRoute) {
        AuthRoute.SignUp.route -> AuthRoute.PhoneNumber.route
        AuthRoute.PhoneNumber.route -> AuthRoute.Email.route
        AuthRoute.Email.route -> AuthRoute.Code.route
        AuthRoute.Code.route -> AuthRoute.ProfileDetails.route
        AuthRoute.ProfileDetails.route -> AuthRoute.Gender.route
        AuthRoute.Gender.route -> AuthRoute.Passions.route
        AuthRoute.Passions.route -> AuthRoute.Friends.route
        AuthRoute.Friends.route -> AuthRoute.Notifications.route
        AuthRoute.Notifications.route -> Routes.DASHBOARD_SCREEN
        else -> null
    }
}