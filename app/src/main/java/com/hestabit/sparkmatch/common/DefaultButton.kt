package com.hestabit.sparkmatch.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    text: String,
    btnColor: Color = HotPink,
    txtColor: Color = White,
    isLoading: Boolean = false,
    enabled: Boolean = true,
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
            contentColor = txtColor,
            disabledContainerColor = btnColor.copy(alpha = 0.5f),
            disabledContentColor = txtColor.copy(alpha = 0.7f)
        ),
        enabled = enabled && !isLoading
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Text is invisible during loading but keeps the button size consistent
            Text(
                text = text,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center),
                color = if (isLoading) Color.Transparent else Color.Unspecified
            )

            // Show loading indicator when loading
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = txtColor,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}