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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.firebase.AuthState
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel

@Composable
fun CreatePasswordScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    identifier: String,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var emailSent by remember { mutableStateOf(false) }

    // Is this an email or phone?
    val isEmail = identifier.contains("@")

    // Check password match and requirements
    val passwordsMatch = password == confirmPassword
    val isValidPassword = password.length >= 8
    val isButtonEnabled = passwordsMatch && isValidPassword && password.isNotBlank() && confirmPassword.isNotBlank()

    // Monitor auth state
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Loading -> {
                isLoading = true
                errorMessage = null
            }
            is AuthState.Authenticated -> {
                isLoading = false
                // For email, proceed to verification screen (email has been sent)
                if (isEmail) {
                    emailSent = true
                    // Optionally navigate to verification instructions screen
                    navController.navigate(AuthRoute.VerifyEmail.route)
                } else {
                    // For phone, go straight to the main app or profile details setup
                    navController.navigate(AuthRoute.ProfileDetails.route)
                }
            }
            is AuthState.Error -> {
                isLoading = false
                errorMessage = (authState as AuthState.Error).message
            }
            else -> {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(paddingValues)
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = "Create a password",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp
            )
            Text(
                text = if (isEmail)
                    "Create a secure password for your account. We'll send a verification email."
                else
                    "Create a secure password for your account.",
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )

            Text(
                text = identifier,
                textAlign = TextAlign.Start,
                fontFamily = modernist,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = HotPink
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
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
                    text = "Create a password",
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
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
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
                    text = "Confirm password",
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
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password requirements
        Text(
            text = "Password must be at least 8 characters",
            color = if (isValidPassword || password.isEmpty()) Color.Gray else Color.Red,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = modernist
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
        )

        // Passwords match check
        if (confirmPassword.isNotEmpty() && !passwordsMatch) {
            Text(
                text = "Passwords don't match",
                color = Color.Red,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = modernist
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 4.dp)
            )
        }

        // Show error message if exists
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = modernist
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Show email sent message
        if (emailSent) {
            Text(
                text = "Verification email sent. Please check your inbox.",
                color = Color.Green,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = modernist
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        DefaultButton(
            text = "Create Account",
            isLoading = isLoading,
            enabled = isButtonEnabled,
            onClick = {
                if (isEmail) {
                    authViewModel.signUp(identifier, password)
                } else {
                    // For phone, we need a different approach since the user is already authenticated
                    // Here we might need to update the user's profile with the password
                    // Or use a custom implementation to handle phone+password
                    navController.navigate(AuthRoute.ProfileDetails.route)
                }
            }
        )
    }
}