package com.hestabit.sparkmatch.screens.auth

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
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
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.data.AuthMethod
import com.hestabit.sparkmatch.data.AuthState
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.*
import com.hestabit.sparkmatch.viewmodel.AuthViewModel

@Composable
fun SignUp(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val authUiState by authViewModel.authUiState.collectAsState()

    // State to track whether we're in signup or login mode
    var isNewUser by remember { mutableStateOf(authUiState.isNewUser) }

    // Update the ViewModel's state when isNewUser changes
    LaunchedEffect(isNewUser) {
        authViewModel.setNewUserState(isNewUser)
    }

    // Handle authentication state changes
    LaunchedEffect(authUiState.authState) {
        when (val authState = authUiState.authState) {
            is AuthState.Authenticated -> {
                onNavigate(Routes.DASHBOARD_SCREEN)
            }
            is AuthState.Error -> {
                // Optional: Handle error state if needed
                Log.e("SignUp", "Authentication Error: ${authState.message}")
            }
            else -> {}
        }
    }

    // If loading, show a progress indicator
    if (authUiState.authState is AuthState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = HotPink)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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

        Spacer(modifier = Modifier.height(58.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = if (isNewUser) "Create account using" else "Sign in using",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = HotPink
            )

            Spacer(modifier = Modifier.height(20.dp))

            DefaultButton(
                text = "Email",
                onClick = {
                    // Set auth method and navigate to email screen
                    authViewModel.setAuthMethod(AuthMethod.EMAIL)
                    onNavigate(AuthRoute.Email.route)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    // Set auth method and navigate to phone number screen
                    authViewModel.setAuthMethod(AuthMethod.PHONE)
                    onNavigate(AuthRoute.PhoneNumber.route)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, OffWhite),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "Phone number",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = HotPink
                )
            }

            Row {
                TextButton(
                    onClick = {
                        // Toggle between signup and login modes
                        isNewUser = !isNewUser
                    }
                ) {
                    Text(
                        text = if (isNewUser) "Already have an account?" else "Don't have an account?",
                        fontFamily = modernist,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = HotPink
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(96.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
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