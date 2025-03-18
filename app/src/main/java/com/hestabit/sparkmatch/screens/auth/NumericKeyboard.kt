package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun NumericKeyboard(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("","0", "⌫")
    )

    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.Transparent)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(72.dp),
        ) {
            keys.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.wrapContentSize()
                ) {
                    row.forEach { key ->
                        Box(
                            modifier = Modifier
                                .size(120.dp,36.dp)
                                .background(Color.Transparent, shape = CircleShape)
                                .clickable {
                                    if (key == "⌫") {
                                        onDeleteClick()
                                    } else {
                                        onNumberClick(key)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (key == "⌫") {
                                Icon(
                                    painter = painterResource(id = R.drawable.backspace),
                                    contentDescription = "Delete",
                                    tint = Color.Black
                                )
                            } else {
                                Text(
                                    text = key,
                                    style = TextStyle(
                                        fontSize = 24.sp,
                                        fontFamily = modernist,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}