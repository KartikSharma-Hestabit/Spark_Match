package com.hestabit.sparkmatch.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DefaultIconButton(iconId: Int) {

    OutlinedIconButton(
        onClick = {},
        border = BorderStroke(2.dp, Color(0xFFF3F3F3)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.size(52.dp)
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = "Icon Button",
            tint = Color(0xffE94057),
            modifier = Modifier.size(18.dp)
        )
    }

}