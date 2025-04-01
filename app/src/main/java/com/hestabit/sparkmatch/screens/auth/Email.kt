package com.hestabit.sparkmatch.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel

@Composable
fun Email(modifier: Modifier = Modifier, authViewModel: AuthViewModel, onNavigate: (String) -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var isNewUser by remember { mutableStateOf(authViewModel.isNewUser.value) }

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
                text =
                    if(!isNewUser){
                        "Enter your email to login"
                    } else {
                        "Please enter your valid email address. We will send you a verification code to activate your account."
                    }
                ,
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Email") },
            shape = RoundedCornerShape(15.dp),
            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = OffWhite,
                focusedBorderColor = Gray,
                unfocusedLabelColor = OffWhite,
                focusedLabelColor = Gray,
                cursorColor = Gray
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if(!isNewUser){
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                shape = RoundedCornerShape(15.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Outlined.Visibility
                    else Icons.Outlined.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = description,
                            tint = HotPink
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = OffWhite,
                    focusedBorderColor = Gray,
                    unfocusedLabelColor = OffWhite,
                    focusedLabelColor = Gray,
                    cursorColor = Gray
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton (
            text = "Continue",
            onClick = {
                if (!isNewUser) {
                    onNavigate(Routes.DASHBOARD_SCREEN)
                } else {
                    onNavigate(AuthRoute.CreatePassword.route)
                }
            }
        )
    }
}

@Preview
@Composable
fun EmailPreview(){
    Email(modifier = Modifier, onNavigate = {}, authViewModel = AuthViewModel(SavedStateHandle()))
}