package com.hestabit.sparkmatch.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.hestabit.sparkmatch.data.LikedBy
import com.hestabit.sparkmatch.data.MatchUser
import com.hestabit.sparkmatch.data.Response
//import com.hestabit.sparkmatch.utils.Utils.convertMapToJsonString
import com.hestabit.sparkmatch.utils.Utils.stringListToPassions
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.utils.Utils.printDebug
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
//import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
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

            val userdata = userProfile.copy(
                passions = passionsToStringList(userProfile.passionsObject),
                passionsObject = emptyList()
            )

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
    override suspend fun updateUserProfile(
        userId: String,
        updates: Map<String, Any>
    ): Result<Unit> {
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

            currentUser = snapshot?.toObject(UserProfile::class.java) ?: currentUser

            trySend(currentUser).isSuccess
        }
        awaitClose { listener.remove() }
    }

    override suspend fun updateLikes(
        userProfile: UserProfile,
        isMatch: Boolean
    ): Response<Boolean> {
        return try {

            usersCollection.document(firebaseAuth.currentUser!!.uid)
                .update("likedList", FieldValue.arrayUnion(userProfile.uid)).await()
            usersCollection.document(userProfile.uid).update(
                "likedByList",
                FieldValue.arrayUnion(
                    LikedBy(
                        uid = currentUser.uid,
                        profileImageUrl = currentUser.profileImageUrl.toString(),
                    )
                )
            ).await()

            if (isMatch) {
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
            }

            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }
}