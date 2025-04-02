package com.hestabit.sparkmatch

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.hestabit.sparkmatch.data.AuthState
import com.hestabit.sparkmatch.router.MainNavigator.InitMainNavigator
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.screens.auth.AuthScreen
import com.hestabit.sparkmatch.ui.theme.SparkMatchTheme
import com.hestabit.sparkmatch.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SparkMatchTheme {
                if (authViewModel.authState == AuthState.Authenticated) {
                    InitMainNavigator(startRoute = Routes.DASHBOARD_SCREEN)
                    Log.d("MainActivity", "Authenticated")
                } else {
                    AuthScreen(authViewModel = authViewModel, onNavigate = {})
                    Log.d("MainActivity", "Not Authenticated")
                }
            }
        }
    }
}