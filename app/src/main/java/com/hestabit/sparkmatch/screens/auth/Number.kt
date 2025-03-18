package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun PhoneNumber(){

    Column (
        modifier = Modifier.fillMaxSize().padding(40.dp,0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(200.dp))

        Column {
            Text(
                text = "My mobile",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please enter your valid phone number. We will send you a 4-digit code to verify your account. ",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .border(
                        2.dp,
                        Color(0xFFF3F3F3),
                        RoundedCornerShape(16.dp)
                    )
            ){
                TextField(
                    value = "",
                    onValueChange = {  },
                    modifier = Modifier.width(56.dp).clip(RoundedCornerShape(6.dp)),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.Transparent,
                        unfocusedTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = modernist,
                        textAlign = TextAlign.Start
                    ),
                    placeholder = {
                        Text(
                            text = "+91",
                            color = Color(0xFF525252),
                            fontSize = 14.sp,
                            fontFamily = modernist
                        )
                    }
                )
                VerticalDivider(
                    color = Color(0xFF525252),
                    thickness = 0.5.dp,
                    modifier = Modifier.height(18.dp)
                )
                TextField(
                    value = "",
                    onValueChange = {  },
                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(6.dp)),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.Transparent,
                        unfocusedTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = modernist,
                        textAlign = TextAlign.Start
                    ),
                    placeholder = {
                        Text(
                            text = "Phone number",
                            color = Color(0xFF525252),
                            fontSize = 16.sp,
                            fontFamily = modernist
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {},
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
                    text = "Continue",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }

}