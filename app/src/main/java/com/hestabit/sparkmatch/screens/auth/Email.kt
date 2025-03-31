package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun Email(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {

    var email by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize().background(White).padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = "My email",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
            )
            Text(
                text = "Please enter your valid email address. We will send you a verification link to activate your account.",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    OffWhite,
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

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton (
            text = "Continue",
            onClick = {
                onNavigate(AuthRoute.Code.route)
            }
        )
    }
}