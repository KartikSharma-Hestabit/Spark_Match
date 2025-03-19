package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.common.CustomButton
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun Gender() {
    var selectedOption by remember { mutableStateOf("Man") }

    Box(modifier = Modifier.fillMaxSize().padding(40.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)
        ) {
            BackButton()
            TextButton(onClick = {}) {
                Text(
                    text = "Skip",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xffE94057)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "I am a",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            Column(modifier = Modifier.padding(top = 16.dp)) {
                GenderSelectionButton(text = "Man", isSelected = selectedOption == "Man") {
                    selectedOption = "Man"
                }
                Spacer(modifier = Modifier.height(8.dp))
                GenderSelectionButton(text = "Woman", isSelected = selectedOption == "Woman") {
                    selectedOption = "Woman"
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            CustomButton("Continue")
        }
    }
}

@Composable
fun GenderSelectionButton(
    text: String,
    isSelected: Boolean,
    showArrow: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xffE94057) else Color(0xFFF5F5F5))
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = if (isSelected) Color.White else Color.Black
        )
        if (isSelected || showArrow) {
            Icon(
                painter = painterResource(
                    if (showArrow) R.drawable.arrow_right else R.drawable.round_check_24
                ),
                contentDescription = null,
                tint = if (isSelected) Color.White else Color.Gray
            )
        }
    }
}