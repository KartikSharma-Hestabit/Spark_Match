package com.hestabit.sparkmatch.firebase

import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface PhoneAuthRepository {
    suspend fun sendVerificationCode(phoneNumber: String, activity: Activity): Flow<PhoneAuthState>
    suspend fun verifyPhoneNumberWithCode(verificationId: String, code: String): Result<FirebaseUser>
    suspend fun signOut(): Result<Boolean>
}

