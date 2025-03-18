package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun Code(){

    Column (
        modifier = Modifier.fillMaxSize().padding(0.dp,44.dp)
    ) {
        Column (
            modifier = Modifier.padding(start = 40.dp)
        ){
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

        Spacer(modifier = Modifier.height(32.dp))

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "00:42",
                    textAlign = TextAlign.Start,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Type the verification code \n" +
                            "we’ve sent you",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row (
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = "",
                    onValueChange = {  },
                    modifier = Modifier
                        .size(67.dp,70.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .border(2.dp, Color(0xFFE8E6EA), RoundedCornerShape(15.dp)),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color(0xFFE94057),
                        unfocusedTextColor = Color(0xFFE94057),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        color = Color(0xFFE8E6EA),
                        fontSize = 34.sp,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )

                TextField(
                    value = "",
                    onValueChange = {  },
                    modifier = Modifier
                        .size(67.dp,70.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .border(2.dp, Color(0xFFE8E6EA), RoundedCornerShape(15.dp)),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color(0xFFE94057),
                        unfocusedTextColor = Color(0xFFE94057),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        color = Color(0xFFE8E6EA),
                        fontSize = 34.sp,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )

                TextField(
                    value = "",
                    onValueChange = {  },
                    modifier = Modifier
                        .size(67.dp,70.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .border(2.dp, Color(0xFFE8E6EA), RoundedCornerShape(15.dp)),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color(0xFFE94057),
                        unfocusedTextColor = Color(0xFFE94057),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        color = Color(0xFFE8E6EA),
                        fontSize = 34.sp,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )

                TextField(
                    value = "",
                    onValueChange = {  },
                    modifier = Modifier
                        .size(67.dp,70.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .border(2.dp, Color(0xFFE8E6EA), RoundedCornerShape(15.dp)),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color(0xFFE94057),
                        unfocusedTextColor = Color(0xFFE94057),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        color = Color(0xFFE8E6EA),
                        fontSize = 34.sp,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            NumericKeyboard(
                onNumberClick = { },
                onDeleteClick = { }
            )
        }

        Spacer(modifier = Modifier.height(54.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            TextButton(
                onClick = {},
            ) {
                Text(
                    text = "Send again",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xffE94057)
                )
            }
        }
    }
}