package com.hestabit.sparkmatch.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit
){
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonColors(
            containerColor = Color(0xffE94057),
            contentColor = Color(0xFFFFFFFF),
            disabledContainerColor = Color(0xffE94057),
            disabledContentColor = Color(0xFFFFFFFF)
        ),
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontFamily = modernist,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFFFFFFFF)
        )
    }
}