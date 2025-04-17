package com.hestabit.sparkmatch

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.hestabit.sparkmatch.common.RequestPermissions
import com.hestabit.sparkmatch.router.MainNavigator.InitMainNavigator
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.SparkMatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SparkMatchTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RequestPermissions { permissions ->
                        val allGranted = permissions.all { it.value }
                        if (!allGranted) {
                            Toast.makeText(
                                this@MainActivity,
                                "Some permissions were denied. App functionality may be limited.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    InitMainNavigator(startRoute = Routes.SPLASH)
                }
            }
        }
    }
}