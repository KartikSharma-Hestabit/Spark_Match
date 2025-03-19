package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hestabit.sparkmatch.R
import com.hestabit.sparkmatch.common.CustomButton
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun SignUp(){

    Column (
        modifier = Modifier.fillMaxSize().padding(40.dp,0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(200.dp))

        Box {
            Image(
                painter = painterResource(id = R.drawable.spark_match_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(130.dp)
            )
        }

        Spacer(modifier = Modifier.height(78.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sign up to continue",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            CustomButton("Continue with email")

            Spacer(modifier = Modifier.height(22.dp))

            TextButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color(0xFFF3F3F3), shape = RoundedCornerShape(16.dp)),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "Use phone number",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xffE94057)
                )
            }

        }

        Spacer(modifier = Modifier.height(78.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HorizontalDivider(modifier = Modifier.width(94.dp))
            Text(
                text = "or sign up with",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color(0xFF000000)
            )
            HorizontalDivider(modifier = Modifier.width(94.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
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
                    painter = painterResource(R.drawable.facebook),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xffE94057)
                )
            }
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
                    painter = painterResource(R.drawable.google),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xffE94057)
                )
            }
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
                    painter = painterResource(R.drawable.apple),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xffE94057)
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        Row {

            TextButton(
                onClick = {}
            ) {
                Text(
                    text = "Terms of use",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color(0xffE94057)
                )
            }

            TextButton(
                onClick = {}
            ) {
                Text(
                    text = "Privacy Policy",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color(0xffE94057)
                )
            }
        }
    }

}