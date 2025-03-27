package com.hestabit.sparkmatch.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.Utils.printDebug
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    text: String,
    btnColor: Color = HotPink,
    txtColor: Color = White,
    onClick: () -> Unit
) {

    TextButton(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = onClick,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = btnColor,
            contentColor = txtColor
        )
    ) {

        Text(text, fontWeight = FontWeight.W700, fontSize = 16.sp)

    }

}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewButton() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DefaultButton(text = "Sample Text") { printDebug("button clicked") }
    }
}