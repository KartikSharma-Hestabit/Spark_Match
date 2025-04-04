package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun Notifications(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    Column (
        modifier = modifier.fillMaxSize().background(White).padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.height(98.dp))
        Image(
            modifier = Modifier.size(240.dp),
            painter = painterResource(R.drawable.chat),
            contentDescription = "People Image"
        )

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "Enable notifications",
            textAlign = TextAlign.Center,
            fontFamily = modernist,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xff000000)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Get push-notification when you get the match or receive a message.",
            textAlign = TextAlign.Center,
            fontFamily = modernist,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color(0xB2000000)
        )

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton (
            text = "I want to be notified",
            onClick = {
                onNavigate(Routes.DASHBOARD_SCREEN)
            }
        )
    }
}