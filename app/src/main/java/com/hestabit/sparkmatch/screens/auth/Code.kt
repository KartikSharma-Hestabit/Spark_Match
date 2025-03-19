package com.hestabit.sparkmatch.screens.auth

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.common.NumericKeyboard
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.modernist
import kotlinx.coroutines.delay

@Composable
fun Code() {
    var timeLeft by remember { mutableIntStateOf(60) }
    var timerFinished by remember { mutableStateOf(false) }

    fun restartTimer() {
        timeLeft = 60
        timerFinished = false
    }

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000L) // 1 second delay
            timeLeft--
        } else {
            timerFinished = true
        }
    }

    Scaffold(
        topBar = {
            Row(modifier = Modifier.padding(start = 40.dp, top = 40.dp)) {
                BackButton()
            }
        },
        bottomBar = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 64.dp)
            ){
                TextButton(
                    onClick = { restartTimer() },
                ) {
                    Text(
                        text = "Send again",
                        textAlign = TextAlign.Center,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = HotPink
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 40.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Type the verification code \nwe’ve sent you",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            var otpCode by remember { mutableStateOf("") }

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
                                if (isNext)
                                    HotPink
                                else if (isFilled)
                                    HotPink
                                else
                                    OffWhite,
                                RoundedCornerShape(15.dp)
                            )
                            .background(if (isFilled) Color(0xFFE94057) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char.ifEmpty { "0" },
                            style = TextStyle(
                                color = when {
                                    isFilled -> Color.White
                                    isNext -> Color(0xFFE94057)
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

            Spacer(modifier = Modifier.height(52.dp))

            NumericKeyboard(
                onNumberClick = { digit ->
                    if (otpCode.length < 4) otpCode += digit
                },
                onDeleteClick = {
                    if (otpCode.isNotEmpty()) otpCode = otpCode.dropLast(1)
                }
            )
        }
    }
}
