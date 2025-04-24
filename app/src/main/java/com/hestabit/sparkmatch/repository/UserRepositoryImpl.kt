package com.hestabit.sparkmatch.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.hestabit.sparkmatch.data.LikedBy
import com.hestabit.sparkmatch.data.MatchUser
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.AuthRoute
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "UserRepositoryImpl"

class UserRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val storageRepository: StorageRepository,
    private val storage: FirebaseStorage
) : UserRepository {
    internal val usersCollection = db.collection("users")

    private lateinit var currentUser: UserProfile

    override fun getCurrentUserProfile(): UserProfile {
        return currentUser
    }

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
        return storageRepository.uploadImage(imageUri, "profile_images")
    }

    override suspend fun saveUserProfile(userId: String, userProfile: UserProfile): Result<Unit> {
        return try {
            val userData = userProfile.copy(
                passions = passionsToStringList(userProfile.passionsObject),
                passionsObject = emptyList()
            )
            usersCollection.document(userId).set(userData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user profile", e)
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(
        userId: String,
        updates: Map<String, Any>
    ): Result<Unit> {
        return try {
            usersCollection.document(userId).set(updates, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user profile", e)
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(userId: String): Response<UserProfile> {
        return try {
            val document = usersCollection.document(userId)
            val data = document.get().await()
            if (data.exists()) {
                val result = data.toObject(UserProfile::class.java)
                if (result != null) {
                    if (userId == firebaseAuth.currentUser?.uid) {
                        currentUser = result
                    }
                    Response.Success(result)
                } else {
                    Response.Failure(Exception("User not found!"))
                }
            } else {
                Response.Failure(Exception("User not found!"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun listenUserUpdates(userId: String): Flow<UserProfile> = callbackFlow {
        val document = usersCollection.document(userId)

        val listener = document.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error) // Will cancel the flow
                return@addSnapshotListener
            }

            snapshot?.toObject(UserProfile::class.java)?.let {
                currentUser = it
                trySend(it).isSuccess
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun updateLikes(
        userProfile: UserProfile,
        isMatch: Boolean
    ): Response<Boolean> {
        return try {


            if (isMatch) {

                // REMOVED FROM CURRENT USER LIKED-BY-LIST (REMOTE USER)
                usersCollection.document(firebaseAuth.currentUser!!.uid)
                    .update(
                        "likedByList", FieldValue.arrayRemove(
                            LikedBy(
                                uid = userProfile.uid,
                                profileImageUrl = userProfile.profileImageUrl.toString(),
                            )
                        )
                    ).await()

                //REMOVED FROM REMOTE USER'S LIKED LIST (CURRENT USER)
                usersCollection.document(userProfile.uid).update(
                    "likedList",
                    FieldValue.arrayRemove(
                        firebaseAuth.currentUser!!.uid
                    )
                ).await()

                // ADDED TO REMOTE USER TO MATCH LIST (CURRENT USER)
                usersCollection.document(firebaseAuth.currentUser!!.uid)
                    .update(
                        "matchList",
                        FieldValue.arrayUnion(
                            MatchUser(
                                userProfile.profileImageUrl.toString(),
                                userProfile.uid,
                                userProfile.firstName,
                                userProfile.birthday
                            )
                        )
                    ).await()

                // ADDED TO CURRENT USER MATCH LIST (REMOTE USER)
                usersCollection.document(userProfile.uid)
                    .update(
                        "matchList",
                        FieldValue.arrayUnion(
                            MatchUser(
                                currentUser.profileImageUrl.toString(),
                                currentUser.uid,
                                currentUser.firstName,
                                currentUser.birthday
                            )
                        )
                    ).await()
            } else {

                // ADDED TO CURRENT USER LIKED-LIST (REMOTE USER)
                usersCollection.document(firebaseAuth.currentUser!!.uid)
                    .update("likedList", FieldValue.arrayUnion(userProfile.uid)).await()

                // ADDED TO REMOTE USER LIKED-BY-LIST (CURRENT USER)
                usersCollection.document(userProfile.uid).update(
                    "likedByList",
                    FieldValue.arrayUnion(
                        LikedBy(
                            uid = currentUser.uid,
                            profileImageUrl = currentUser.profileImageUrl.toString(),
                        )
                    )
                ).await()
            }

            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
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