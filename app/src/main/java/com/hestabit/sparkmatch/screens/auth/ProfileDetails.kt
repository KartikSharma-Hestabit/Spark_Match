package com.hestabit.sparkmatch.screens.auth

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.hestabit.sparkmatch.common.CustomTextField
import com.hestabit.sparkmatch.common.ProfileImagePicker
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun ProfileDetails(){

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    Box (
        modifier = Modifier.fillMaxSize().padding(40.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth().align(Alignment.TopEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            TextButton(
                onClick = {},
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
        }

        Column (
            modifier = Modifier.padding(top = 126.dp)
        ){
            Text(
                text = "Profile details",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
            )

            Spacer(modifier = Modifier.height(90.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProfileImagePicker(
                    imageUri = null,
                    onImageClick = {}
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomTextField(label = "First name", value = firstName, onValueChange = { firstName = it })
                CustomTextField(label = "Last name", value = lastName, onValueChange = { lastName = it })
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonColors(
                    containerColor = Color(0x1AE94057),
                    contentColor = Color(0xFFFFFFFF),
                    disabledContainerColor = Color(0xffE94057),
                    disabledContentColor = Color(0xFFFFFFFF)
                ),
                contentPadding = PaddingValues(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.calender),
                        contentDescription = "Calendar Icon",
                        tint = Color(0xffE94057),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Choose birthday date",
                        textAlign = TextAlign.Center,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xffE94057)
                    )
                }
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            verticalAlignment = Alignment.Bottom
        ){
            CustomButton("Confirm")
        }

    }
}