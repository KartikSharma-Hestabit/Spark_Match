package com.hestabit.sparkmatch.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.OffWhite

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = Gray) },
        shape = RoundedCornerShape(15.dp),
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 14.sp
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = OffWhite,
            focusedBorderColor = Gray,
            unfocusedLabelColor = OffWhite,
            focusedLabelColor = OffWhite,
            cursorColor = Gray,
        ),
        modifier = Modifier.fillMaxWidth(),
    )
}