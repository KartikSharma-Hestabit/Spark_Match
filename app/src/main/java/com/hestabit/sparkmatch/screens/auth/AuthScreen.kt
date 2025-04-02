package com.hestabit.sparkmatch.screens.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.data.AuthState
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.MainNavigator.InitAuthNavigator
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.router.Routes.getNextAuthRoute
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(authViewModel: AuthViewModel, onNavigate: (String) -> Unit) {
    val authNavController = rememberNavController()
    val currentRoute = authNavController.currentBackStackEntryAsState().value?.destination?.route
    val nextRoute = getNextAuthRoute(currentRoute)

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val authState by authViewModel.authState.observeAsState()
    var showSkipButton by remember { mutableStateOf(true) }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                onNavigate(Routes.DASHBOARD_SCREEN)
            }
            is AuthState.Error -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        (authState as AuthState.Error).message
                    )
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(currentRoute) {
        showSkipButton = when (currentRoute) {
            AuthRoute.SignUp.route, Routes.DASHBOARD_SCREEN,
            AuthRoute.Email.route, AuthRoute.PhoneNumber.route,
            AuthRoute.Code.route -> false
            else -> true
        }
    }

    Scaffold(
        containerColor = White,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { /* Empty title */ },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                navigationIcon = {
                    if (currentRoute != AuthRoute.SignUp.route && currentRoute != Routes.DASHBOARD_SCREEN) {
                        BackButton(authNavController, HotPink)
                    }
                },
                actions = {
                    if (showSkipButton) {
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
                },
                modifier = Modifier.padding(20.dp)
            )
        }
    ) { paddingValues ->
        InitAuthNavigator(
            modifier = Modifier.padding(paddingValues),
            authNavController = authNavController,
            authViewModel = authViewModel,
            onNavigate = { route ->
                if (route == Routes.DASHBOARD_SCREEN) {
                    onNavigate(route)
                } else {
                    authNavController.navigate(route)
                }
            }
        )
    }
}