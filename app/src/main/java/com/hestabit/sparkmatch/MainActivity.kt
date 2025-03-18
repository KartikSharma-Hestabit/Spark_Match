package com.hestabit.sparkmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hestabit.sparkmatch.routing.MainNavigator.InitMainNavigator
import com.hestabit.sparkmatch.routing.Routes
import com.hestabit.sparkmatch.ui.theme.SparkMatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SparkMatchTheme {
                InitMainNavigator(startRoute = Routes.DISCOVER_SCREEN)
            }
        }
    }
}
