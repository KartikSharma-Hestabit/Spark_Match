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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.common.CustomButton
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun Passions(){

    val options = listOf(
        "Photography", "Shopping", "Karaoke", "Yoga", "Cooking", "Tennis",
        "Run", "Swimming", "Art", "Traveling", "Extreme", "Music", "Drink", "Video games"
    )

    var selectedOptions by remember { mutableStateOf(setOf<String>()) }

    Box (
        modifier = Modifier.fillMaxSize().padding(0.dp,48.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(start = 40.dp, end = 40.dp)
        ){
            BackButton()

            TextButton(
                onClick = {},
            ) {
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

        Spacer(modifier = Modifier.height(32.dp))

        Column (
            modifier = Modifier.padding(start = 40.dp, end = 40.dp).align(Alignment.Center)
        ){
            Text(
                text = "Your interests",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
            )

            Text(
                text = "Select a few of your interests and let everyone know what you’re passionate about.",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                options.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowItems.forEach { item ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)  // Ensures uniform height
                            ) {
                                PassionSelectionButton(
                                    text = item,
                                    isSelected = selectedOptions.contains(item)
                                ) {
                                    selectedOptions = if (selectedOptions.contains(item)) {
                                        selectedOptions - item
                                    } else {
                                        if (selectedOptions.size < 3) selectedOptions + item else selectedOptions
                                    }
                                }
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f)) // Ensures alignment in case of odd elements
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }



        Row (
            modifier = Modifier.padding(start = 40.dp, end = 40.dp).fillMaxWidth().align(Alignment.BottomCenter),
            verticalAlignment = Alignment.Bottom
        ){
            CustomButton("Continue")
        }
    }
}

@Composable
fun PassionSelectionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0xffE94057) else Color(0xFFF5F5F5))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = modernist,
            fontWeight = FontWeight.Normal,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}