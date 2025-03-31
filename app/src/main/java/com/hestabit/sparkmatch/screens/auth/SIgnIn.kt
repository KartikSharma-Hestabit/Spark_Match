package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.firebase.AuthState
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.Black
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel

@Composable
fun SignIn(
    navController: NavController,
    paddingValues: PaddingValues,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            navController.navigate(Routes.DASHBOARD_SCREEN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(White).padding(horizontal = 40.dp).padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.spark_match_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(78.dp))

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Text(
                text = "Sign up to continue",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            DefaultButton (
                text = "Continue with email",
                onClick = {
                    navController.navigate(AuthRoute.Email.route)
                }
            )

            OutlinedButton(
                onClick = {
                    navController.navigate(AuthRoute.PhoneNumber.route)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, OffWhite),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "Use phone number",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = HotPink
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Gray, thickness = 0.5.dp)
            Text(
                text = "or sign up with",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Black,
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Gray, thickness = 0.5.dp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedButton(
                onClick = {
                    // Implement Facebook sign-in (requires additional setup)
                },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, OffWhite),
                contentPadding = PaddingValues(16.dp)
            )  {
                Icon(
                    painter = painterResource(R.drawable.facebook),
                    contentDescription = "Facebook Icon",
                    tint = HotPink,
                    modifier = Modifier.size(32.dp)
                )
            }
            OutlinedButton(
                onClick = {
                    // Implement Google sign-in (requires additional setup)
                },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, OffWhite),
                contentPadding = PaddingValues(16.dp)
            )  {
                Icon(
                    painter = painterResource(R.drawable.google),
                    contentDescription = "Google Icon",
                    tint = HotPink,
                    modifier = Modifier.size(32.dp)
                )
            }
            OutlinedButton(
                onClick = {
                    // Implement Apple sign-in (requires additional setup)
                },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, OffWhite),
                contentPadding = PaddingValues(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.apple),
                    contentDescription = "Apple Icon",
                    tint = HotPink,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(76.dp))

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ){
            TextButton(onClick = {}) {
                Text(
                    text = "Terms of Use",
                    fontFamily = modernist,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = HotPink
                )
            }
            TextButton(onClick = {}) {
                Text(
                    text = "Privacy Policy",
                    fontFamily = modernist,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = HotPink
                )
            }
        }
    }
}