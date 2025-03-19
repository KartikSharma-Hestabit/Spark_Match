package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.common.CustomButton
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun Email() {
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.padding(start = 40.dp, top = 40.dp)
            ) {
                BackButton()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 40.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Text(
                    text = "My email",
                    textAlign = TextAlign.Start,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Please enter your valid email address. We will send you a verification link to activate your account.",
                    textAlign = TextAlign.Start,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            2.dp,
                            Color(0xFFF3F3F3),
                            RoundedCornerShape(16.dp)
                        ),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = modernist,
                        textAlign = TextAlign.Start
                    ),
                    placeholder = {
                        Text(
                            text = "Enter your email",
                            color = Color(0xFF525252),
                            fontSize = 16.sp,
                            fontFamily = modernist
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    visualTransformation = VisualTransformation.None
                )

                Spacer(modifier = Modifier.height(64.dp))

                CustomButton("Continue") {
                    // Handle email submission logic here
                }
            }
        }
    }
}