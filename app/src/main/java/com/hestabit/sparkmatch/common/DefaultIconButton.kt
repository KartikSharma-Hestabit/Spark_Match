package com.hestabit.sparkmatch.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hestabit.sparkmatch.R

@Composable
fun DefaultIconButton(iconId: Int, iconTint: Color = Color(0xffE94057),containerColor: Color = Color.Unspecified, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {

    OutlinedIconButton(
        onClick = onClick,
        border = BorderStroke(2.dp, Color(0xFFF3F3F3)),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.size(52.dp),
        colors = IconButtonDefaults.outlinedIconButtonColors(containerColor = containerColor)
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = "Icon Button",
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview(){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        DefaultIconButton(R.drawable.arrow_left){}
    }
}