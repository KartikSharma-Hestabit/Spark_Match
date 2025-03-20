package com.hestabit.sparkmatch.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.ui.theme.OffWhite

@Composable
fun BackButton(navController: NavController, color: Color){
    OutlinedButton(
        onClick = { navController.popBackStack() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, OffWhite),
        contentPadding = PaddingValues(16.dp)
    )  {
        Icon(
            painter = painterResource(R.drawable.back),
            contentDescription = "Facebook Icon",
            tint = color,
            modifier = Modifier.size(28.dp)
        )
    }
}