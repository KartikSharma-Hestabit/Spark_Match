package com.hestabit.sparkmatch.firebase

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : PhoneAuthRepository {

    override suspend fun sendVerificationCode(phoneNumber: String, activity: Activity): Flow<PhoneAuthState> = callbackFlow {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                trySend(PhoneAuthState.VerificationCompleted(credential))
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                trySend(PhoneAuthState.VerificationFailed(exception.message ?: "Verification failed"))
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                trySend(PhoneAuthState.CodeSent(verificationId, token))
            }

            override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
                trySend(PhoneAuthState.TimeOut(verificationId))
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose()
    }

    override suspend fun verifyPhoneNumberWithCode(verificationId: String, code: String): Result<FirebaseUser> {
        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            val result = auth.signInWithCredential(credential).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkIfPhoneUserExists(phoneNumber: String): Result<Boolean> {
        // Firebase doesn't have a direct method to check if a phone user exists
        // For sake of implementation, we'll return false, but in a real app
        // you might need to use Firestore to track registered phone numbers
        return Result.success(false)
    }

    override suspend fun signInWithPhoneCredential(credential: PhoneAuthCredential): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithCredential(credential).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun linkPhoneWithEmail(credential: PhoneAuthCredential): Result<FirebaseUser> {
        val currentUser = auth.currentUser
        return if (currentUser != null) {
            try {
                val result = currentUser.linkWithCredential(credential).await()
                Result.success(result.user!!)
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            Result.failure(Exception("No user currently signed in"))
        }
    }

    override suspend fun signOut(): Result<Boolean> {
        return try {
            auth.signOut()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}