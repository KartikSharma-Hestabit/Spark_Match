package com.hestabit.sparkmatch.screens.auth

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hestabit.sparkmatch.common.NumericKeyboard
import com.hestabit.sparkmatch.firebase.PhoneUiState
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.PhoneAuthViewModel
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun Code(modifier: Modifier = Modifier, phoneAuthViewModel: PhoneAuthViewModel = hiltViewModel(), onNavigate: (String) -> Unit) {
    var timeLeft by remember { mutableIntStateOf(60) }
    var timerFinished by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val phoneAuthState by phoneAuthViewModel.phoneAuthState.collectAsState()

    fun restartTimer() {
        timeLeft = 60
        timerFinished = false
    }

    // Monitor phone auth state
    LaunchedEffect(phoneAuthState) {
        when (phoneAuthState) {
            is PhoneUiState.Loading -> {
                isLoading = true
                errorMessage = null
            }
            is PhoneUiState.Authenticated -> {
                isLoading = false
                // Navigate to create password screen
//                onNavigate(AuthRoute.CreatePassword.route.replace("{identifier}", ))
            }
            is PhoneUiState.Error -> {
                isLoading = false
                errorMessage = (phoneAuthState as PhoneUiState.Error).message
            }
            else -> {
                isLoading = false
            }
        }
    }

    // Timer effect
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000L) // Delay 1 second
            timeLeft--
        } else if (!timerFinished) {
            timerFinished = true
        }
    }

    Column(
        modifier = modifier.fillMaxSize().background(White).padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format(Locale.getDefault(), "%02d:%02d", timeLeft / 60, timeLeft % 60),
            textAlign = TextAlign.Center,
            fontFamily = modernist,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Type the verification code \nwe've sent you",
            textAlign = TextAlign.Center,
            fontFamily = modernist,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // OTP Input Display
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(4) { index ->
                val char = otpCode.getOrNull(index)?.toString() ?: ""
                val isFilled = char.isNotEmpty()
                val isNext = index == otpCode.length

                Box(
                    modifier = Modifier
                        .size(67.dp, 70.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .border(
                            2.dp,
                            if (isNext) HotPink else if (isFilled) HotPink else OffWhite,
                            RoundedCornerShape(15.dp)
                        )
                        .background(if (isFilled) HotPink else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isFilled) char else "",
                        style = TextStyle(
                            color = when {
                                isFilled -> Color.White
                                isNext -> HotPink
                                else -> Color(0xFFE8E6EA)
                            },
                            fontSize = 34.sp,
                            fontFamily = modernist,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }

        // Show error message if exists
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage!!,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = modernist
                ),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        NumericKeyboard(
            currentInput = otpCode,
            maxLength = 4,
            onNumberClick = { digit ->
                if (otpCode.length < 4) otpCode += digit
            },
            onDeleteClick = {
                if (otpCode.isNotEmpty()) otpCode = otpCode.dropLast(1)
            },
            onComplete = {
                phoneAuthViewModel.verifyCode(otpCode)
                onNavigate(AuthRoute.ProfileDetails.route)
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 64.dp)
        ) {
            TextButton(
                onClick = {
                    restartTimer()
//                    phoneAuthViewModel.resendVerificationCode(context as Activity)
                },
                enabled = timerFinished && !isLoading
            ) {
                Text(
                    text = "Send again",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (timerFinished && !isLoading) HotPink else Color.Gray
                )
            }
        }
    }
}
