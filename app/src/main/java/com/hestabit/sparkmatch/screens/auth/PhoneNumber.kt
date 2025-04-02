package com.hestabit.sparkmatch.screens.auth

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hestabit.sparkmatch.common.CountryPickerBottomSheet
import com.hestabit.sparkmatch.common.DefaultButton
import com.hestabit.sparkmatch.data.AuthState
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.ui.theme.Gray
import com.hestabit.sparkmatch.ui.theme.HotPink
import com.hestabit.sparkmatch.ui.theme.OffWhite
import com.hestabit.sparkmatch.ui.theme.White
import com.hestabit.sparkmatch.ui.theme.modernist
import com.hestabit.sparkmatch.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@SuppressLint("ContextCastToActivity")
@Composable
fun PhoneNumber(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onNavigate: (String) -> Unit
) {
    // Context for Firebase
    val context = LocalContext.current as Activity

    // State
    var countryCode by remember { mutableStateOf("+1") }
    var phoneNumber by remember { mutableStateOf("") }
    var isDialogOpen by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(false) }
    var isVerificationInProgress by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // Validate phone number
    fun validatePhoneNumber(): Boolean {
        if (phoneNumber.isBlank()) {
            phoneError = "Phone number is required"
            return false
        }

        if (phoneNumber.length < 8) {
            phoneError = "Please enter a valid phone number"
            return false
        }

        if (!phoneNumber.all { it.isDigit() }) {
            phoneError = "Phone number should only contain digits"
            return false
        }

        phoneError = ""
        return true
    }

    // Handle verification callbacks
    val callbacks = remember {
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This will be called if the verification is completed automatically
                // (uncommon on most devices now due to security)
                isVerificationInProgress = false
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Navigate to next screen
                            onNavigate(AuthRoute.ProfileDetails.route)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    task.exception?.message ?: "Verification failed"
                                )
                            }
                        }
                    }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                isVerificationInProgress = false
                scope.launch {
                    snackbarHostState.showSnackbar("Verification failed: ${e.message}")
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                isVerificationInProgress = false
                // Save verification ID to AuthViewModel
                authViewModel.setVerificationId(verificationId)
                // Navigate to code verification screen
                onNavigate(AuthRoute.Code.route)
            }
        }
    }

    // Start phone verification
    fun startPhoneVerification() {
        if (!validatePhoneNumber()) return

        isVerificationInProgress = true

        val fullPhoneNumber = "$countryCode$phoneNumber".trim()

        // Set up PhoneAuth options
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(fullPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(context)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
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
                    .border(1.dp, if (phoneError.isEmpty()) OffWhite else Color.Red, RoundedCornerShape(16.dp))
                    .padding(10.dp)
            ) {
                // Country Code Button
                TextButton(
                    onClick = { isDialogOpen = true },
                    modifier = Modifier.wrapContentSize(),
                    colors = ButtonDefaults.textButtonColors(containerColor = Color.Transparent),
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
                            if (phoneError.isNotEmpty()) phoneError = ""
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

            // Error message
            if (phoneError.isNotEmpty()) {
                Text(
                    text = phoneError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontFamily = modernist,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 4.dp)
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

            DefaultButton(
                text = if (isVerificationInProgress) "Sending Code..." else "Continue",
                enabled = !isVerificationInProgress && phoneNumber.isNotEmpty() && selected,
                onClick = { startPhoneVerification() }
            )
        }

        // Show loading indicator if auth is in progress
        if (isVerificationInProgress) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = HotPink)
            }
        }

        // Snackbar for error messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}