package com.hestabit.sparkmatch

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hestabit.sparkmatch.data.AuthState
import com.hestabit.sparkmatch.router.MainNavigator.InitMainNavigator
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.HotPink
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
                val authState by authViewModel.authState.observeAsState()
                when (authState) {
                    is AuthState.Authenticated -> {
                        InitMainNavigator(startRoute = Routes.DASHBOARD_SCREEN)
                    }

                    is AuthState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = HotPink)
                        }
                    }

                    is AuthState.Unauthenticated -> {
                        InitMainNavigator(startRoute = Routes.ONBOARDING_SCREEN)
                    }

                    is AuthState.Error -> {

                    }

                    null -> TODO()
                }
            }
        }
    }
}