package com.hestabit.sparkmatch.screens.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.MainNavigator.InitAuthNavigator
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.router.Routes.getNextAuthRoute
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(authViewModel: AuthViewModel,onNavigate: (String) -> Unit) {

    val authNavController = rememberNavController()
    val currentRoute = authNavController.currentBackStackEntryAsState().value?.destination?.route
    val nextRoute = getNextAuthRoute(currentRoute)

    Scaffold(
        containerColor = White,
        topBar = {
            TopAppBar(
                title = { /* You can add a title if needed */ },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    if (currentRoute != AuthRoute.SignUp.route && currentRoute != Routes.DASHBOARD_SCREEN) {
                        BackButton(authNavController, HotPink)
                    }
                },
                actions = {
                    if (currentRoute != AuthRoute.SignUp.route && currentRoute != Routes.DASHBOARD_SCREEN) {
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
    ) { paddingValues ->
        InitAuthNavigator(
            modifier = Modifier.padding(paddingValues),
            authNavController = authNavController,
            authViewModel = authViewModel,
            onNavigate = onNavigate
        )
    }
}

