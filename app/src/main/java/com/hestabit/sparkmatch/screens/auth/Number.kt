package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.common.CustomButton
import com.hestabit.sparkmatch.ui.theme.Black
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun PhoneNumber(){

    var countryCode by remember { mutableStateOf("") }

    var phoneNumber by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.padding(start = 40.dp, top = 40.dp)
            ) {
                BackButton()
            }
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 40.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        value = countryCode,
                        onValueChange = { countryCode = it },
                        modifier = Modifier.width(56.dp).clip(RoundedCornerShape(6.dp)),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
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
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp)),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(
                            color = Black,
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

                Spacer(modifier = Modifier.height(64.dp))

                CustomButton(
                    text = "Continue",
                    onClick = {

                    }
                )
            }
        }
    }
}