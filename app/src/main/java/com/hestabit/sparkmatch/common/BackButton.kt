package com.hestabit.sparkmatch.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hestabit.sparkmatch.R

@Composable
fun BackButton(){
    IconButton(
        onClick = {},
        modifier = Modifier
            .size(64.dp)
            .border(
                2.dp,
                Color(0xFFF3F3F3),
                RoundedCornerShape(16.dp)
            )
    ) {
        Icon(
            painter = painterResource(R.drawable.back),
            contentDescription = "Google Icon",
            modifier = Modifier.size(28.dp),
            tint = Color(0xffE94057)
        )
    }
}