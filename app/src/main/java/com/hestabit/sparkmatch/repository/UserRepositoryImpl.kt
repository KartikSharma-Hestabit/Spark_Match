package com.hestabit.sparkmatch.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.AuthRoute
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    db: FirebaseFirestore,
    private val storageRepository: StorageRepository
) : UserRepository {

    private val TAG = "UserRepositoryImpl"
    internal val usersCollection = db.collection("users")

    override fun usersCollection(): CollectionReference {
        return usersCollection
    }

    override fun passionsToStringList(passions: List<AuthRoute.PassionType>): List<String> {
        return passions.map { passion ->
            passion.id
        }
    }

    override suspend fun uploadProfileImage(imageUri: Uri?): String? {
        if (imageUri == null) return null
        val imageRef = storageRepository.uploadImage(imageUri, "profile_images")
        return imageRef
    }

    override suspend fun saveUserProfile(userId: String, userProfile: UserProfile): Result<Unit> {
        return try {
            val userData = userProfile.copy(passions = passionsToStringList(userProfile.passionsObject), passionsObject = emptyList())
            usersCollection.document(userId).set(userData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user profile", e)
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            usersCollection.document(userId).set(updates, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user profile", e)
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val document = usersCollection.document(userId).get().await()
            if (document.exists()) {
                document.toObject(UserProfile::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user profile", e)
            null
        }
    }

    override suspend fun saveBasicProfileDetails(
        userId: String,
        firstName: String,
        lastName: String,
        birthday: String,
        homeTown: String,
        profileImageUri: Uri?
    ): Result<Unit> {
        return try {
            val basicUserData = hashMapOf(
                "uid" to userId,
                "firstName" to firstName,
                "lastName" to lastName,
                "birthday" to birthday,
                "homeTown" to homeTown
            )

            // Upload profile image if provided
            if (profileImageUri != null) {
                val imageUrl = storageRepository.uploadImage(profileImageUri, "profile_images")
                if (imageUrl != null) {
                    basicUserData["profileImageUrl"] = imageUrl
                }
            }

            // Update the user profile
            usersCollection.document(userId)
                .set(basicUserData, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving basic profile details", e)
            Result.failure(e)
        }
    }

    override suspend fun saveGenderSelection(userId: String, gender: String): Result<Unit> {
        return try {
            val genderData = hashMapOf("gender" to gender)
            usersCollection.document(userId)
                .set(genderData, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving gender selection", e)
            Result.failure(e)
        }
    }

    override suspend fun saveInterestPreference(userId: String, preference: String): Result<Unit> {
        return try {
            val preferenceData = hashMapOf("interestPreference" to preference)
            usersCollection.document(userId)
                .set(preferenceData, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving interest preference", e)
            Result.failure(e)
        }
    }

    override suspend fun savePassions(
        userId: String,
        passions: List<AuthRoute.PassionType>
    ): Result<Unit> {
        return try {
            val passionStrings = passionsToStringList(passions)
            val passionsData = hashMapOf("passions" to passionStrings)

            usersCollection.document(userId)
                .set(passionsData, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving passions", e)
            Result.failure(e)
        }
    }

    override suspend fun saveAboutDetails(
        userId: String,
        profession: String,
        about: String
    ): Result<Unit> {
        return try {
            val aboutData = mapOf(
                "profession" to profession,
                "about" to about
            )

            usersCollection.document(userId)
                .set(aboutData, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving about details", e)
            Result.failure(e)
        }
    }

    override suspend fun updateProfileDetails(
        userId: String,
        originalProfile: UserProfile,
        updatedProfile: UserProfile
    ): Result<Unit> {
        try {
            // Create a map to hold only the fields that have changed
            val updatedFields = mutableMapOf<String, Any>()

            // Check each field to see if it has changed
            if (updatedProfile.firstName != originalProfile.firstName) {
                updatedFields["firstName"] = updatedProfile.firstName
            }

            if (updatedProfile.lastName != originalProfile.lastName) {
                updatedFields["lastName"] = updatedProfile.lastName
            }

            if (updatedProfile.homeTown != originalProfile.homeTown) {
                updatedFields["homeTown"] = updatedProfile.homeTown
            }

            if (updatedProfile.gender != originalProfile.gender) {
                updatedFields["gender"] = updatedProfile.gender
            }

            if (updatedProfile.interestPreference != originalProfile.interestPreference) {
                updatedFields["interestPreference"] = updatedProfile.interestPreference
            }

            if (updatedProfile.profession != originalProfile.profession) {
                updatedFields["profession"] = updatedProfile.profession
            }

            if (updatedProfile.about != originalProfile.about) {
                updatedFields["about"] = updatedProfile.about
            }

            // Add location field comparison
            if (updatedProfile.location != originalProfile.location) {
                updatedFields["location"] = updatedProfile.location
            }

            // Compare passions lists to check if they're different
            val originalPassionSet = originalProfile.passions.toSet()
            val updatedPassionSet = passionsToStringList(updatedProfile.passionsObject).toSet()

            if (originalPassionSet != updatedPassionSet) {
                // Convert passions to the string list format expected by Firestore
                updatedFields["passions"] = passionsToStringList(updatedProfile.passionsObject)
            }

            // Handle gallery images
            if (updatedProfile.galleryImages != originalProfile.galleryImages) {
                updatedFields["galleryImages"] = updatedProfile.galleryImages
            }

            // Handle profile image separately as it requires special processing
            if (updatedProfile.profileImage != originalProfile.profileImage) {
                val imageUrl = storageRepository.uploadImage(
                    updatedProfile.profileImage!!,
                    "profile_images"
                )
                if (imageUrl != null) {
                    updatedFields["profileImageUrl"] = imageUrl
                }
            }

            // If there are no updates, return success immediately
            if (updatedFields.isEmpty()) {
                return Result.success(Unit)
            }

            // Update the user profile with all changed fields
            return updateUserProfile(userId, updatedFields)

        } catch (e: Exception) {
            Log.e(TAG, "Error updating profile details", e)
            return Result.failure(e)
        }
    }
}