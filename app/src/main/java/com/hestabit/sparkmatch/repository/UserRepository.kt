package com.hestabit.sparkmatch.repository

import android.net.Uri
import com.google.firebase.firestore.CollectionReference
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.AuthRoute

interface UserRepository {

    fun usersCollection(): CollectionReference

    fun passionsToStringList(passions: List<AuthRoute.PassionType>): List<String>

    suspend fun uploadProfileImage(imageUri: Uri?): String?

    suspend fun saveUserProfile(userId: String, userProfile: UserProfile): Result<Unit>

    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit>

    suspend fun getUserProfile(userId: String): UserProfile?

    // New methods to add from ProfileDetailsViewModel
    suspend fun saveBasicProfileDetails(
        userId: String,
        firstName: String,
        lastName: String,
        birthday: String,
        homeTown: String,
        profileImageUri: Uri?
    ): Result<Unit>

    suspend fun saveGenderSelection(userId: String, gender: String): Result<Unit>

    suspend fun saveInterestPreference(userId: String, preference: String): Result<Unit>

    suspend fun savePassions(userId: String, passions: List<AuthRoute.PassionType>): Result<Unit>

    suspend fun saveAboutDetails(userId: String, profession: String, about: String): Result<Unit>

    suspend fun updateProfileDetails(
        userId: String,
        originalProfile: UserProfile,
        updatedProfile: UserProfile
    ): Result<Unit>
}