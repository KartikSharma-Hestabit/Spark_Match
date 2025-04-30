package com.hestabit.sparkmatch.screens.auth

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.data.AuthState
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.router.Routes
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun Email(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    // Added state for reset password functionality
    var showResetPassword by remember { mutableStateOf(false) }
    var resetPasswordEmail by remember { mutableStateOf("") }
    var resetPasswordVisible by remember { mutableStateOf(false) }
    var resetPasswordError by remember { mutableStateOf("") }

    val authUiState by authViewModel.authUiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun validateEmail(): Boolean {
        if (email.isBlank()) {
            emailError = "Email is required"
            return false
        }
        if (!email.matches(emailRegex)) {
            emailError = "Please enter a valid email address"
            return false
        }
        emailError = ""
        return true
    }

    fun validateResetPasswordEmail(): Boolean {
        if (resetPasswordEmail.isBlank()) {
            resetPasswordError = "Email is required"
            return false
        }
        if (!resetPasswordEmail.matches(emailRegex)) {
            resetPasswordError = "Please enter a valid email address"
            return false
        }
        resetPasswordError = ""
        return true
    }

    fun validatePassword(): Boolean {
        passwordError = ""

        if (password.length < 8) {
            passwordError = "Password must be at least 8 characters"
            return false
        }

        val isNewUser = authUiState.isNewUser

        if (isNewUser) {
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
        }

        return true
    }

    LaunchedEffect(authUiState.authState) {
        when (val authState = authUiState.authState) {
            is AuthState.Authenticated -> {
                if (authUiState.isNewUser) {
                    onNavigate(AuthRoute.ProfileDetails.route)
                } else {
                    onNavigate(Routes.DASHBOARD_SCREEN)
                }
            }
            is AuthState.Error -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        authState.message,
                        duration = SnackbarDuration.Long
                    )
                }
            }
            is AuthState.Unauthenticated -> {
                email = ""
                password = ""
                confirmPassword = ""
                emailError = ""
                passwordError = ""
                showResetPassword = false
                resetPasswordEmail = ""
                resetPasswordError = ""
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(White)
                .padding(40.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (showResetPassword) "Reset Password" else "My email",
                    textAlign = TextAlign.Start,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp
                )
                Text(
                    text = if (showResetPassword) {
                        "Enter your email address and we'll send you a reset link."
                    } else if (!authUiState.isNewUser) {
                        "Enter your email to login"
                    } else {
                        "Please enter your valid email address. We will send you a verification code to activate your account."
                    },
                    textAlign = TextAlign.Start,
                    fontFamily = modernist,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (showResetPassword) {
                // Reset Password UI
                OutlinedTextField(
                    value = resetPasswordEmail,
                    onValueChange = {
                        resetPasswordEmail = it
                        if (resetPasswordError.isNotEmpty()) resetPasswordError = ""
                    },
                    singleLine = true,
                    label = { Text("Email Address") },
                    shape = RoundedCornerShape(15.dp),
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = resetPasswordError.isNotEmpty(),
                    supportingText = {
                        if (resetPasswordError.isNotEmpty()) {
                            Text(resetPasswordError, color = Color.Red)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = OffWhite,
                        focusedBorderColor = Gray,
                        unfocusedLabelColor = OffWhite,
                        focusedLabelColor = Gray,
                        cursorColor = Gray,
                        errorBorderColor = Color.Red,
                        errorLabelColor = Color.Red
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // Regular Login/Signup UI
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (emailError.isNotEmpty()) emailError = ""
                    },
                    singleLine = true,
                    label = { Text("Email Address") },
                    shape = RoundedCornerShape(15.dp),
                    textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = emailError.isNotEmpty(),
                    supportingText = {
                        if (emailError.isNotEmpty()) {
                            Text(emailError, color = Color.Red)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = OffWhite,
                        focusedBorderColor = Gray,
                        unfocusedLabelColor = OffWhite,
                        focusedLabelColor = Gray,
                        cursorColor = Gray,
                        errorBorderColor = Color.Red,
                        errorLabelColor = Color.Red
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (!authUiState.isNewUser) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (passwordError.isNotEmpty()) passwordError = ""
                        },
                        singleLine = true,
                        label = { Text("Password") },
                        shape = RoundedCornerShape(15.dp),
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = passwordError.isNotEmpty(),
                        supportingText = {
                            if (passwordError.isNotEmpty()) {
                                Text(passwordError, color = Color.Red)
                            }
                        },
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
                            cursorColor = Gray,
                            errorBorderColor = Color.Red,
                            errorLabelColor = Color.Red
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Add Forgot Password text button for login screen
                    TextButton(
                        onClick = { showResetPassword = true },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "Forgot Password?",
                            color = HotPink,
                            fontFamily = modernist,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                if (passwordError.isNotEmpty()) passwordError = ""
                            },
                            singleLine = true,
                            label = { Text("Password") },
                            shape = RoundedCornerShape(15.dp),
                            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            isError = passwordError.isNotEmpty(),
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
                                cursorColor = Gray,
                                errorBorderColor = Color.Red,
                                errorLabelColor = Color.Red
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                confirmPassword = it
                                if (passwordError.isNotEmpty()) passwordError = ""
                            },
                            singleLine = true,
                            label = { Text("Confirm Password") },
                            shape = RoundedCornerShape(15.dp),
                            textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            isError = passwordError.isNotEmpty(),
                            supportingText = {
                                if (passwordError.isNotEmpty()) {
                                    Text(passwordError, color = Color.Red)
                                }
                            },
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
                                cursorColor = Gray,
                                errorBorderColor = Color.Red,
                                errorLabelColor = Color.Red
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            DefaultButton(
                text = if (authUiState.authState is AuthState.Loading) "Please wait..." else
                    if (showResetPassword) "Reset Password" else "Continue",
                enabled = if (showResetPassword) {
                    authUiState.authState !is AuthState.Loading && resetPasswordEmail.isNotEmpty()
                } else {
                    authUiState.authState !is AuthState.Loading &&
                            email.isNotEmpty() &&
                            password.isNotEmpty() &&
                            (!authUiState.isNewUser || confirmPassword.isNotEmpty())
                },
                onClick = {
                    if (showResetPassword) {
                        if (validateResetPasswordEmail()) {
                            Log.d("EmailScreen", "Attempting password reset")
                            Log.d("EmailScreen", "Email: $resetPasswordEmail")
                            authViewModel.resetPassword(resetPasswordEmail)
                            scope.launch {
                                snackBarHostState.showSnackbar(
                                    "Password reset email has been sent to $resetPasswordEmail",
                                    duration = SnackbarDuration.Long
                                )
                            }
                            showResetPassword = false
                        }
                    } else {
                        if (validateEmail() && validatePassword()) {
                            Log.d("EmailScreen", "Attempting authentication")
                            Log.d("EmailScreen", "Email: $email")
                            Log.d("EmailScreen", "Is New User: ${authUiState.isNewUser}")

                            if (!authUiState.isNewUser) {
                                Log.d("EmailScreen", "Calling login")
                                authViewModel.login(email, password)
                            } else {
                                Log.d("EmailScreen", "Calling signup")
                                authViewModel.signUp(email, password)
                            }
                        }
                    }
                }
            )

            if (showResetPassword) {
                TextButton(
                    onClick = { showResetPassword = false },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "Back to Login",
                        color = HotPink,
                        fontFamily = modernist,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }

        if (authUiState.authState is AuthState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = HotPink)
            }
        }

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}