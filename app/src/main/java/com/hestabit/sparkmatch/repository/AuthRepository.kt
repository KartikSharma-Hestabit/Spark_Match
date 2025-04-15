package com.hestabit.sparkmatch.repository

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import com.hestabit.sparkmatch.data.Response

interface AuthRepository {

    var verificationId: String

    suspend fun getUser(): Response<FirebaseUser?>

    fun isLoggedIn(): Boolean

    suspend fun login(email: String, password: String): Response<Boolean>

    suspend fun signUp(email: String, password: String): Response<Boolean>

    suspend fun signOut(): Response<Boolean>

    suspend fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit)

    suspend fun updateProfile(displayName: String? = null,
                              photoUri: String? = null,
                              onSuccess: () -> Unit,
                              onFailure: (String) -> Unit)

    suspend fun verifyPhoneNumber(phoneNumber: String,
                                  verificationCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks)

    suspend fun verifyCode(code: String, onComplete: (Boolean, String?) -> Unit)

    // Remove this method as it conflicts with the property setter
    // fun setVerificationId(id: String)
}