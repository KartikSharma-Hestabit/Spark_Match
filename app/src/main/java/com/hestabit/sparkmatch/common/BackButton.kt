package com.hestabit.sparkmatch.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White

@Composable
fun BackButton(){
    OutlinedIconButton(
        onClick = {},
        modifier = Modifier
            .size(64.dp)
            .border(2.dp, White, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            2.dp,
            OffWhite
        )
    ) {
        Icon(
            painter = painterResource(R.drawable.back),
            contentDescription = "Facebook Icon",
            tint = HotPink,
            modifier = Modifier.size(28.dp)
        )
    }
}