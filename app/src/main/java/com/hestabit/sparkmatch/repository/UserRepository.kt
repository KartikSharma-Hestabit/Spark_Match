package com.hestabit.sparkmatch.repository

import android.net.Uri
import com.google.firebase.firestore.CollectionReference
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.AuthRoute

interface UserRepository {

    fun usersCollection(): CollectionReference

    fun passionsToStringList(passions: List<AuthRoute.PassionType>): List<String>

//    fun stringListToPassions(passionStrings: List<String>): List<AuthRoute.PassionType>

    suspend fun uploadProfileImage(imageUri: Uri?): String?

    suspend fun saveUserProfile(userId: String, userProfile: UserProfile): Result<Unit>

    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit>

    suspend fun getUserProfile(userId: String): UserProfile?

}