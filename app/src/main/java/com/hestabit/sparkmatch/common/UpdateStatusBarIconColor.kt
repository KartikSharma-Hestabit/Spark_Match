package com.hestabit.sparkmatch.common

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat

@Composable
fun UpdateStatusBarIconColor(darkIcons: Boolean) {
    val window = (LocalActivity.current as Activity).window
    SideEffect {
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = darkIcons
    }
}