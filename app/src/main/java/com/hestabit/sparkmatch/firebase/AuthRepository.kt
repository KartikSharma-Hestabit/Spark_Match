package com.hestabit.sparkmatch.firebase

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
    val currentUserId: String
    val hasUser: Boolean

    val currentUserFlow: Flow<FirebaseUser?>

    suspend fun signInWithEmailPassword(email: String, password: String): Result<FirebaseUser>
    suspend fun signUpWithEmailPassword(email: String, password: String): Result<FirebaseUser>
    suspend fun sendEmailVerification(): Result<Boolean>
    suspend fun checkIfUserExists(email: String): Result<Boolean>
    suspend fun sendPasswordResetEmail(email: String): Result<Boolean>
    suspend fun signOut(): Result<Boolean>
    suspend fun deleteAccount(): Result<Boolean>
}