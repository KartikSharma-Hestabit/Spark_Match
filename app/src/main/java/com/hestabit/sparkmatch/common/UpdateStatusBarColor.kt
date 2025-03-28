package com.hestabit.sparkmatch.common

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat

@Composable
fun UpdateStatusBarColor(color: Color, darkIcons: Boolean = true) {
    val window = (LocalActivity.current as Activity).window
    SideEffect {
        window.statusBarColor = color.toArgb()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = darkIcons
    }
}