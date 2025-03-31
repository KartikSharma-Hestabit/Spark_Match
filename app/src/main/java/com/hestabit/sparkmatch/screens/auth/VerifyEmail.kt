// app/src/main/java/com/hestabit/sparkmatch/screens/auth/VerifyEmail.kt
package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel

@Composable
fun VerifyEmail(
    navController: NavController,
    paddingValues: PaddingValues,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(paddingValues)
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.chat),
            contentDescription = "Email Verification",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Verify your email",
            fontFamily = modernist,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "We've sent a verification email to your inbox. Please check your email and click the verification link to continue.",
            fontFamily = modernist,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(40.dp))

        DefaultButton(
            text = "Continue to Profile Setup",
            onClick = {
                // Since the user is already authenticated but needs to verify email,
                // we'll let them proceed to profile setup
                navController.navigate(AuthRoute.ProfileDetails.route)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DefaultButton(
            text = "Resend Email",
            btnColor = HotPink.copy(alpha = 0.1f),
            txtColor = HotPink,
            onClick = {
//                authViewModel.sendEmailVerification()
            }
        )
    }
}