package com.hestabit.sparkmatch.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val modernist = FontFamily(
    Font(R.font.sk_modernist_regular, FontWeight.Normal),
    Font(R.font.sk_modernist_bold, FontWeight.Bold),
    Font(R.font.sk_modernist_mono, FontWeight.Thin),
)