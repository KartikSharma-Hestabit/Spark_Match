package com.hestabit.sparkmatch.firebase

import android.app.Activity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.Flow

interface PhoneAuthRepository {
    suspend fun sendVerificationCode(phoneNumber: String, activity: Activity): Flow<PhoneAuthState>
    suspend fun verifyPhoneNumberWithCode(verificationId: String, code: String): Result<FirebaseUser>
    suspend fun checkIfPhoneUserExists(phoneNumber: String): Result<Boolean>
    suspend fun signInWithPhoneCredential(credential: PhoneAuthCredential): Result<FirebaseUser>
    suspend fun linkPhoneWithEmail(credential: PhoneAuthCredential): Result<FirebaseUser>
    suspend fun signOut(): Result<Boolean>
}