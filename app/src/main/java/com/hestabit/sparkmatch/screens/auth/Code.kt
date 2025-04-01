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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.common.NumericKeyboard
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.min

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Code(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val horizontalPadding = (screenWidth * 0.05f).dp
    val verticalPadding = (screenHeight * 0.03f).dp
    val boxHeight = min(60f, screenHeight * 0.08f).dp
    val spacingBetweenBoxes = min(8f, screenWidth * 0.01f).dp
    val timerFontSize = min(34f, screenWidth * 0.08f).sp
    val headerTextSize = min(18f, screenWidth * 0.045f).sp
    var timeLeft by remember { mutableIntStateOf(60) }
    var timerFinished by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun restartTimer() {
        timeLeft = 60
        timerFinished = false
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

    // Check if OTP is complete and navigate
    LaunchedEffect(otpCode) {
        if (otpCode.length == 6) {
            isLoading = true
            // You can add verification logic here before navigating
            // For example, validate the OTP with Firebase
            delay(500) // Simulating verification
            onNavigate(AuthRoute.ProfileDetails.route)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = String.format(Locale.getDefault(), "%02d:%02d", timeLeft / 60, timeLeft % 60),
            textAlign = TextAlign.Center,
            fontFamily = modernist,
            fontWeight = FontWeight.Bold,
            fontSize = timerFontSize
        )

        Spacer(modifier = Modifier.height((screenHeight * 0.01f).dp))

        Text(
            text = "Type the verification code \nwe've sent you",
            textAlign = TextAlign.Center,
            fontFamily = modernist,
            fontWeight = FontWeight.Normal,
            fontSize = headerTextSize
        )

        Spacer(modifier = Modifier.height((screenHeight * 0.05f).dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 400.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingBetweenBoxes),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(6) { index ->
                    val char = otpCode.getOrNull(index)?.toString() ?: ""
                    val isFilled = char.isNotEmpty()
                    val isNext = index == otpCode.length

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(boxHeight)
                            .clip(RoundedCornerShape(min(15f, screenWidth * 0.03f).dp))
                            .border(
                                min(2f, screenWidth * 0.005f).dp,
                                if (isNext) HotPink else if (isFilled) HotPink else OffWhite,
                                RoundedCornerShape(min(15f, screenWidth * 0.03f).dp)
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
                                fontSize = min(24f, screenWidth * 0.06f).sp,
                                fontFamily = modernist,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }

        // Show error message if exists
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height((screenHeight * 0.02f).dp))
            Text(
                text = errorMessage!!,
                color = Color.Red,
                style = TextStyle(
                    fontSize = min(14f, screenWidth * 0.035f).sp,
                    fontFamily = modernist
                ),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height((screenHeight * 0.04f).dp))

        // Numeric keyboard with constrained width
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 400.dp), // Max width for larger screens
            contentAlignment = Alignment.Center
        ) {
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
                    onNavigate(AuthRoute.ProfileDetails.route)
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f, fill = true).height((screenHeight * 0.04f).dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = (screenHeight * 0.04f).dp)
        ) {
            TextButton(
                onClick = {
                    restartTimer()
                },
                enabled = timerFinished && !isLoading
            ) {
                Text(
                    text = "Send again",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = min(16f, screenWidth * 0.04f).sp,
                    color = if (timerFinished && !isLoading) HotPink else Color.Gray
                )
            }
        }
    }
}