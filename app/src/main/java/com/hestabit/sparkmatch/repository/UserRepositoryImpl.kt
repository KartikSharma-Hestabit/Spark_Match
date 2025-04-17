package com.hestabit.sparkmatch.repository

import android.net.Uri
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
//import com.hestabit.sparkmatch.utils.Utils.convertMapToJsonString
import com.hestabit.sparkmatch.utils.Utils.stringListToPassions
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.AuthRoute
import kotlinx.coroutines.tasks.await
//import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val db: FirebaseFirestore, private val storage: FirebaseStorage) : UserRepository {
    internal val usersCollection = db.collection("users")

    override fun usersCollection(): CollectionReference {
        return usersCollection
    }

    /**
     * Helper method to safely convert passion enum to string representation
     * that can be stored in Firestore
     */
    override fun passionsToStringList(passions: List<AuthRoute.PassionType>): List<String> {
        return passions.map { passion ->
            // Use the id property from our updated enum
            passion.id
        }
    }

    /**
     * Uploads the user's profile image to Firebase Storage
     * @param imageUri The URI of the image to upload
     * @return The download URL of the uploaded image
     */
    override suspend fun uploadProfileImage(imageUri: Uri?): String? {
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
    override suspend fun saveUserProfile(userId: String, userProfile: UserProfile): Result<Unit> {
        return try {

//            //TODO: Need to change implementation of this method
//            // Upload profile image if provided
//            val imageUrl = uploadProfileImage(userProfile.profileImage)
//
//            // Convert UserProfile to a map for Firestore
//            val userData = hashMapOf(
//                "firstName" to userProfile.firstName,
//                "lastName" to userProfile.lastName,
//                "profileImageUrl" to imageUrl,
//                "birthday" to userProfile.birthday,
//                "interestPreference" to userProfile.interestPreference,
//                "location" to userProfile.location,
//                "profession" to userProfile.profession,
//                "about" to userProfile.about,
//                "gender" to userProfile.gender,
//                "passions" to passionsToStringList(userProfile.passionsObject)
//            )

            val userdata = userProfile.copy(passions = passionsToStringList(userProfile.passionsObject), passionsObject = emptyList())

            // Save to Firestore
            usersCollection.document(userId).set(userdata).await()
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
    override suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit> {
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
    override suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val document = usersCollection.document(userId).get().await()
            if (document.exists()) {

                val result = document.toObject(UserProfile::class.java)

                result

            }else{
                null
            }

        } catch (e: Exception) {
            null
        }
    }
}