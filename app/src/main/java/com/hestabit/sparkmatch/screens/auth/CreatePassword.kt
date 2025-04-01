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
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist

@Composable
fun CreatePassword(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit
){
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf("") }

    fun validatePassword(): Boolean {
        passwordError = ""

        if (password.length < 8) {
            passwordError = "Password must be at least 8 characters"
            return false
        }

        if (!password.any { it.isUpperCase() }) {
            passwordError = "Password must contain at least one uppercase letter"
            return false
        }

        if (!password.any { it.isLowerCase() }) {
            passwordError = "Password must contain at least one lowercase letter"
            return false
        }

        if (!password.any { it.isDigit() }) {
            passwordError = "Password must contain at least one number"
            return false
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            passwordError = "Password must contain at least one special character"
            return false
        }

        if (password != confirmPassword) {
            passwordError = "Passwords do not match"
            return false
        }

        return true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Password",
            textAlign = TextAlign.Start,
            fontFamily = modernist,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (passwordError.isNotEmpty()) passwordError = ""
                },
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

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (passwordError.isNotEmpty()) passwordError = ""
                },
                label = { Text("Confirm Password") },
                shape = RoundedCornerShape(15.dp),
                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible)
                        Icons.Outlined.Visibility
                    else Icons.Outlined.VisibilityOff

                    val description = if (confirmPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
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

            if (passwordError.isNotEmpty()) {
                Text(
                    text = passwordError,
                    color = HotPink,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            text = "Confirm",
            enabled = password.isNotEmpty() && confirmPassword.isNotEmpty() && validatePassword(),
            onClick = {
                if (validatePassword()) {
                    onNavigate(AuthRoute.Gender.route)
                }
            }
        )
    }
}

@Preview
@Composable
fun CreatePassPreview(){
    CreatePassword(onNavigate = {})
}