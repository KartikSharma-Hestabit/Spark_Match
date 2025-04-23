package com.hestabit.sparkmatch

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.hestabit.sparkmatch.screens.auth.PhotoUpload
import com.hestabit.sparkmatch.ui.theme.SparkMatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            SparkMatchTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PhotoUpload(onNavigate = {})
                }
            }
        }
    }
}