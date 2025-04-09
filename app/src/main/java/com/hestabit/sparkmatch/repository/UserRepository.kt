package com.hestabit.sparkmatch.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.AuthRoute
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    internal val usersCollection = db.collection("users")

    /**
     * Helper method to safely convert passion enum to string representation
     * that can be stored in Firestore
     */
    private fun passionsToStringList(passions: List<AuthRoute.PassionType>): List<String> {
        return passions.map { passion ->
            // Use the id property from our updated enum
            passion.id
        }
    }

    /**
     * Helper method to convert string representations back to PassionType enums
     */
    private fun stringListToPassions(passionStrings: List<String>): List<AuthRoute.PassionType> {
        return passionStrings.mapNotNull { passionString ->
            try {
                // Use the helper method from our updated enum
                AuthRoute.PassionType.fromId(passionString)
            } catch (e: Exception) {
                Log.e("UserRepository", "Error converting passion string: $passionString", e)
                null
            }
        }
    }

    /**
     * Uploads the user's profile image to Firebase Storage
     * @param imageUri The URI of the image to upload
     * @return The download URL of the uploaded image
     */
    suspend fun uploadProfileImage(imageUri: Uri?): String? {
        if (imageUri == null) return null

        val imageRef = storage.reference.child("profile_images/${UUID.randomUUID()}")
        return try {
            imageRef.putFile(imageUri).await()
            imageRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Saves user profile data to Firestore
     * @param userId The unique ID of the user
     * @param userProfile The user profile data to save
     * @return Result indicating success or failure
     */
    suspend fun saveUserProfile(userId: String, userProfile: UserProfile): Result<Unit> {
        return try {
            // Upload profile image if provided
            val imageUrl = uploadProfileImage(userProfile.profileImage)

            // Convert UserProfile to a map for Firestore
            val userData = hashMapOf(
                "firstName" to userProfile.firstName,
                "lastName" to userProfile.lastName,
                "profileImageUrl" to imageUrl,
                "birthday" to userProfile.birthday,
                "gender" to userProfile.gender,
                "passions" to passionsToStringList(userProfile.passions)
            )

            // Save to Firestore
            usersCollection.document(userId).set(userData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Updates specific fields of a user profile
     * @param userId The unique ID of the user
     * @param updates Map of field names to updated values
     * @return Result indicating success or failure
     */
    suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            // Use set with merge option instead of update
            usersCollection.document(userId).set(updates, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves a user profile from Firestore
     * @param userId The unique ID of the user
     * @return The user profile data or null if not found
     */
    suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val document = usersCollection.document(userId).get().await()
            if (document.exists()) {
                val data = document.data ?: return null

                // Convert passion strings back to enum values
                val passionStrings = data["passions"] as? List<String> ?: emptyList()
                val passions = stringListToPassions(passionStrings)

                UserProfile(
                    firstName = data["firstName"] as? String ?: "",
                    lastName = data["lastName"] as? String ?: "",
                    profileImage = (data["profileImageUrl"] as? String)?.let { Uri.parse(it) },
                    birthday = data["birthday"] as? String ?: "",
                    gender = data["gender"] as? String ?: "",
                    passions = passions
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}