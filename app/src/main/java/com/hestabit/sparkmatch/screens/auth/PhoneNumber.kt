package com.hestabit.sparkmatch.screens.auth

 import androidx.compose.foundation.background
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
 import androidx.compose.foundation.layout.wrapContentSize
 import androidx.compose.foundation.shape.RoundedCornerShape
 import androidx.compose.foundation.text.KeyboardOptions
 import androidx.compose.material3.ButtonDefaults
 import androidx.compose.material3.Text
 import androidx.compose.material3.TextButton
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
 import androidx.compose.ui.text.input.ImeAction
 import androidx.compose.ui.text.input.KeyboardType
 import androidx.compose.ui.text.style.TextAlign
 import androidx.compose.ui.unit.dp
 import androidx.compose.ui.unit.sp
 import com.hestabit.sparkmatch.common.CountryPickerBottomSheet
 import com.hestabit.sparkmatch.common.DefaultButton
 import com.hestabit.sparkmatch.router.AuthRoute
 import com.hestabit.sparkmatch.ui.theme.Gray
 import com.hestabit.sparkmatch.ui.theme.OffWhite
 import com.hestabit.sparkmatch.ui.theme.White
 import com.hestabit.sparkmatch.ui.theme.modernist
 import com.hestabit.sparkmatch.viewmodel.AuthViewModel

@Composable
fun PhoneNumber(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {

    var countryCode by remember { mutableStateOf("Country") }
    var phoneNumber by remember { mutableStateOf("") }
    var isDialogOpen by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize().background(White).padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "My mobile",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
            )
            Text(
                text = "Please enter your valid phone number. We will send you a 6-digit code to verify your account.",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .border(1.dp, OffWhite, RoundedCornerShape(16.dp))
                .padding(10.dp)
        ) {
            // Country Code Button
            TextButton(
                onClick = {
                    isDialogOpen = true
                },
                modifier = Modifier.wrapContentSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                Text(
                    text = countryCode,
                    fontSize = 16.sp,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Normal,
                    color = if (selected) Color.Black else Gray
                )
            }

            VerticalDivider(
                modifier = Modifier
                    .height(30.dp)
                    .width(1.dp),
                color = OffWhite
            )

            TextField(
                value = phoneNumber,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) {
                        phoneNumber = input
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp)),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
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
                    color = if (phoneNumber.isNotEmpty()) Color.Black else Gray,
                    fontSize = 16.sp,
                    fontFamily = modernist,
                    textAlign = TextAlign.Start
                ),
                placeholder = {
                    Text(
                        text = "Phone number",
                        color = Gray,
                        fontSize = 16.sp,
                        fontFamily = modernist
                    )
                }
            )
        }

        // Country Picker Dialog
        if (isDialogOpen) {
            CountryPickerBottomSheet(
                isVisible = isDialogOpen,
                onDismiss = { isDialogOpen = false },
                onSelect = { code ->
                        countryCode = code
                    isDialogOpen = false
                    selected = true
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton (
            text = "Continue",
            onClick = {
                onNavigate(AuthRoute.Code.route)
            }
        )
    }
}