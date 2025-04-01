package com.hestabit.sparkmatch.screens.auth

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel

@Composable
fun SignUp(modifier: Modifier = Modifier,authViewModel: AuthViewModel, onNavigate: (String) -> Unit) {
    var isNewUser by remember { mutableStateOf(true) }
    val viewModelIsNewUser by authViewModel.isNewUser.collectAsState(initial = true)

    LaunchedEffect(viewModelIsNewUser) {
        isNewUser = viewModelIsNewUser
        Log.d("SignUp", "ViewModel isNewUser: $viewModelIsNewUser")
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = if (isNewUser) "Create account using" else "Sign in using",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = HotPink
            )

            DefaultButton(
                text = "Email",
                onClick = {
                    authViewModel.setAuthMethod(AuthViewModel.AuthMethod.EMAIL)
                    onNavigate(AuthRoute.Email.route)
                }
            )

            OutlinedButton(
                onClick = {
                    authViewModel.setAuthMethod(AuthViewModel.AuthMethod.PHONE)
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

@Preview
@Composable
fun SignUpPreview() {
//    SignUp(onNavigate = {})
}