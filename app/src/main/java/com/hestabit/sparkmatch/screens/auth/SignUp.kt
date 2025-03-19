package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.hestabit.sparkmatch.router.MainNavigator.InitMainNavigator
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun SignUp() {
    Scaffold (
        bottomBar = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 40.dp)
            ){
                TextButton(onClick = {}) {
                    Text(
                        text = "Terms of Use",
                        fontFamily = modernist,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = HotPink
                    )
                }
                TextButton(onClick = {}) {
                    Text(
                        text = "Privacy Policy",
                        fontFamily = modernist,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = HotPink
                    )
                }
            }
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 40.dp, vertical = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.spark_match_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(130.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Sign up to continue",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomButton(
                text = "Continue with email",
                onClick = {
                    (Routes.PHONE_NUMBER)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    2.dp,
                    OffWhite
                ) ,
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "Use phone number",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = HotPink
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = OffWhite, thickness = 1.dp)
                Text(
                    text = "or sign up with",
                    textAlign = TextAlign.Center,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = OffWhite, thickness = 1.dp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            val borderedIconButtonModifier = Modifier
                .size(64.dp)
                .border(2.dp, White, RoundedCornerShape(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                OutlinedIconButton(
                    onClick = {},
                    modifier = borderedIconButtonModifier,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        2.dp,
                        OffWhite
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.facebook),
                        contentDescription = "Facebook Icon",
                        tint = HotPink,
                        modifier = Modifier.size(28.dp)
                    )
                }
                OutlinedIconButton(
                    onClick = {},
                    modifier = borderedIconButtonModifier,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        2.dp,
                        OffWhite
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.google),
                        contentDescription = "Google Icon",
                        tint = HotPink,
                        modifier = Modifier.size(28.dp)
                    )
                }
                OutlinedIconButton(
                    onClick = {},
                    modifier = borderedIconButtonModifier,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        2.dp,
                        OffWhite
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.apple),
                        contentDescription = "Apple Icon",
                        tint = HotPink,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}