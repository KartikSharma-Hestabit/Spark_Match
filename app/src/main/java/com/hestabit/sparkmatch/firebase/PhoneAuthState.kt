package com.hestabit.sparkmatch.firebase

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

sealed class PhoneAuthState {
    data class VerificationCompleted(val credential: PhoneAuthCredential) : PhoneAuthState()
    data class CodeSent(val verificationId: String, val token: PhoneAuthProvider.ForceResendingToken) : PhoneAuthState()
    data class VerificationFailed(val message: String) : PhoneAuthState()
    data class TimeOut(val verificationId: String) : PhoneAuthState()
}