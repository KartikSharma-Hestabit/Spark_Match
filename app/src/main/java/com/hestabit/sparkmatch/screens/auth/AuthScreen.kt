package com.hestabit.sparkmatch.screens.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.MainNavigator.InitAuthNavigator
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(authViewModel: AuthViewModel, onNavigate: (String) -> Unit) {

    val authNavController = rememberNavController()
    val currentRoute = authNavController.currentBackStackEntryAsState().value?.destination?.route
    val snackBarHostState = remember { SnackbarHostState() }

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
                }else {
                    authNavController.navigate(route)
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun AuthPreview(){
    AuthScreen(
        AuthViewModel(),
        onNavigate = {}
    ) 
}