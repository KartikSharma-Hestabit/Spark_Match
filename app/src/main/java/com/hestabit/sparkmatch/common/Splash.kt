package com.hestabit.sparkmatch.common

import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.router.MainNavigator.InitMainNavigator
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel
import kotlin.getValue

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun Splash(modifier: Modifier = Modifier, onNavigate: (String) -> Unit){

    val authViewModel: AuthViewModel = hiltViewModel()

    Column (
        modifier.fillMaxSize().background(White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (authViewModel.isLoggedIn()){
            InitMainNavigator(startRoute = Routes.DASHBOARD_SCREEN)
        } else
            InitMainNavigator(startRoute = Routes.ONBOARDING_SCREEN)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.spark_match_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = "Spark Match",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFF5722),
                            HotPink,
                            Color(0xFF673AB7)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, 0f)
                    )
                )
            )
        }
    }
}