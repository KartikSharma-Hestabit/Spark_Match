package com.hestabit.sparkmatch.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun MessageCell(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(R.drawable.img_1),
            contentDescription = "",
            modifier = Modifier
                .size(65.dp)
                .border(
                    2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xff8A2387),
                            Color(0xffE94057),
                            Color(0xffF27121)
                        ),
                        start = Offset(Float.POSITIVE_INFINITY, 0f), // Top-End (Top-Right)
                        end = Offset(0f, Float.POSITIVE_INFINITY) // Bottom-Start (Bottom-Left)
                    ),
                    CircleShape
                )
                .padding(4.dp)
                .clip(
                    CircleShape
                )
                .clickable { /* onNavigate(Routes.STORIES) */ },
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(vertical = 7.dp)
                .weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("Emelie", fontFamily = modernist, fontWeight = FontWeight.W700, fontSize = 14.sp)
            Text(
                "Sticker 😍",
                fontFamily = modernist,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp
            )
        }

        Column(
            modifier = Modifier.padding(vertical = 7.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                "23 min",
                fontFamily = modernist,
                fontWeight = FontWeight.W700,
                fontSize = 12.sp,
                color = Gray
            )
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(HotPink),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "1",
                    fontFamily = modernist,
                    fontWeight = FontWeight.W700,
                    fontSize = 12.sp,
                    color = White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

    }

}