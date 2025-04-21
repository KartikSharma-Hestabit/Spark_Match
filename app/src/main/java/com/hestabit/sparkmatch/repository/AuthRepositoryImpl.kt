package com.hestabit.sparkmatch.repository

import android.app.Activity
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hestabit.sparkmatch.data.Response
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override var verificationId: String = ""

    override suspend fun getUser(): Response<FirebaseUser?> {
        return try {
            Response.Success(firebaseAuth.currentUser)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Error getting user", e)
            Response.Failure(e)
        }
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun login(email: String, password: String): Response<Boolean> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                Log.d("AuthRepositoryImpl", "Login successful for user: ${currentUser.email}")
                Response.Success(true)
            } else {
                Log.e("AuthRepositoryImpl", "Login failed: No current user after sign-in")
                Response.Failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Login error", e)
            Response.Failure(e)
        }
    }

    override suspend fun signUp(email: String, password: String): Response<Boolean> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                Log.d("AuthRepositoryImpl", "Signup successful for user: ${currentUser.email}")
                Response.Success(true)
            } else {
                Log.e("AuthRepositoryImpl", "Signup failed: No current user after creation")
                Response.Failure(Exception("Signup failed"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Signup error", e)
            Response.Failure(e)
        }
    }

    override suspend fun signOut(): Response<Boolean> {
        return try {
            if (firebaseAuth.currentUser != null) {
                firebaseAuth.signOut()
                Response.Success(true)
            } else {
                Response.Failure(Exception("User does not exist"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun resetPassword(
        email: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure(task.exception?.message ?: "Failed to send reset email")
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            onFailure(e.message ?: "Error sending reset email")
        }
    }

    override suspend fun updateProfile(
        displayName: String?,
        photoUri: String?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            val user = firebaseAuth.currentUser
            if (user == null) {
                onFailure("No authenticated user")
                return
            }

            val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder().apply {
                if (displayName != null) setDisplayName(displayName)
                photoUri?.let { setPhotoUri(it.toUri()) }
            }.build()

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure(task.exception?.message ?: "Failed to update profile")
                    }
                }
        } catch (e: Exception) {
            onFailure(e.message ?: "Error updating profile")
        }
    }

    override suspend fun verifyPhoneNumber(
        phoneNumber: String,
        verificationCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        try {
            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(verificationCallbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        } catch (e: Exception) {
            e.printStackTrace()
            verificationCallbacks.onVerificationFailed(
                FirebaseException("Failed to send verification code: ${e.message}")
            )
        }
    }

    override suspend fun verifyCode(code: String, onComplete: (Boolean, String?) -> Unit) {
        try {
            if (verificationId.isEmpty()) {
                onComplete(false, "Verification ID is invalid")
                return
            }

            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete(true, null)
                    } else {
                        onComplete(false, task.exception?.message)
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            onComplete(false, e.message)
        }
    }
}