package com.hestabit.sparkmatch.screens.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hestabit.sparkmatch.common.NumericKeyboard
import com.hestabit.sparkmatch.data.AuthState
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight", "DefaultLocale")
@Composable
fun Code(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    var otpCode by remember { mutableStateOf("") }
    var timeLeft by remember { mutableIntStateOf(60) }
    var timerFinished by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val authUiState by authViewModel.authUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Timer effect
    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
        timerFinished = true
    }

    // Authentication state effect
    LaunchedEffect(authUiState.authState) {
        when (val authState = authUiState.authState) {
            is AuthState.Authenticated -> {
                onNavigate(AuthRoute.ProfileDetails.route)
            }
            is AuthState.Error -> {
                errorMessage = authState.message
                scope.launch {
                    snackbarHostState.showSnackbar(
                        authState.message,
                        duration = SnackbarDuration.Long
                    )
                }
            }
            else -> {}
        }
    }

    // Automatic navigation when OTP is complete
    LaunchedEffect(otpCode) {
        if (otpCode.length == 6 && authUiState.authState !is AuthState.Loading) {
            authViewModel.verifyCode(otpCode)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(White)
                .padding(horizontal = 40.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Type the verification code \nwe've sent you",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // OTP Input Boxes
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(6) { index ->
                    val char = otpCode.getOrNull(index)?.toString() ?: ""
                    val isFilled = char.isNotEmpty()
                    val isNext = index == otpCode.length

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .border(
                                width = 1.dp,
                                color = when {
                                    isNext -> HotPink
                                    isFilled -> HotPink
                                    else -> OffWhite
                                },
                                shape = RoundedCornerShape(15.dp)
                            )
                            .background(
                                color = if (isFilled) HotPink else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = TextStyle(
                                color = if (isFilled) White else Color.Black,
                                fontSize = 24.sp,
                                fontFamily = modernist,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            // Error message
            errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontFamily = modernist,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Numeric Keyboard
            NumericKeyboard(
                currentInput = otpCode,
                maxLength = 6,
                onNumberClick = { digit ->
                    if (otpCode.length < 6) otpCode += digit
                },
                onDeleteClick = {
                    if (otpCode.isNotEmpty()) otpCode = otpCode.dropLast(1)
                },
                onComplete = {
                    // Optional additional action on complete
                    if (otpCode.length == 6) {
                        authViewModel.verifyCode(otpCode)
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Resend Code Option
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = {
                        if (timerFinished && authUiState.authState !is AuthState.Loading) {
                            // TODO: Implement code resend logic
                            // For now, just reset the timer
                            timeLeft = 60
                            timerFinished = false
                            otpCode = ""
                            errorMessage = null
                        }
                    },
                    enabled = timerFinished && authUiState.authState !is AuthState.Loading
                ) {
                    Text(
                        text = "Resend Code",
                        textAlign = TextAlign.Center,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (timerFinished && authUiState.authState !is AuthState.Loading)
                            HotPink
                        else
                            Color.Gray
                    )
                }
            }
        }

        if (authUiState.authState is AuthState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = HotPink)
            }
        }

        // Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}