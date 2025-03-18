package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.hestabit.sparkmatch.common.BackButton
import com.hestabit.sparkmatch.common.CustomButton
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun Friends() {
    Box(modifier = Modifier.fillMaxSize().padding(40.dp)) {
        TextButton(
            onClick = {},
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text(
                text = "Skip",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xffE94057)
            )
        }

        Column (
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                modifier = Modifier.size(240.dp),
                painter = painterResource(R.drawable.people),
                contentDescription = "People Image"
            )

            Text(
                text = "Search friend’s",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xff000000)
            )

            Text(
                text = "You can find friends from your contact lists\n" +
                        "to connected",
                textAlign = TextAlign.Center,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color(0xB2000000)
            )
        }



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            CustomButton("Access to a contact list")
        }
    }
}