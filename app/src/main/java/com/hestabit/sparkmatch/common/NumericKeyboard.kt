package com.hestabit.sparkmatch.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        listOf("", "0", "⌫")
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            keys.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEach { key ->
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clickable {
                                    if (key == "⌫") onDeleteClick() else if (key.isNotEmpty()) onNumberClick(key)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (key == "⌫") {
                                Icon(
                                    painter = painterResource(id = R.drawable.backspace),
                                    contentDescription = "Delete",
                                    tint = Color.Black
                                )
                            } else if (key.isNotEmpty()) {
                                Text(
                                    text = key,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = modernist,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}