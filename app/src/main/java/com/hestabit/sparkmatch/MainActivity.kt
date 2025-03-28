package com.hestabit.sparkmatch

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.hestabit.sparkmatch.common.UpdateStatusBarColor
import com.hestabit.sparkmatch.router.MainNavigator.InitMainNavigator
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.router.authNavHost.AuthNavigator
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.screens.auth.AuthScreen
import com.hestabit.sparkmatch.ui.theme.SparkMatchTheme
import com.hestabit.sparkmatch.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SparkMatchTheme {
                AuthScreen()
            }
        }
    }
}