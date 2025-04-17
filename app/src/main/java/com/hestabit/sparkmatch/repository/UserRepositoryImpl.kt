package com.hestabit.sparkmatch.repository

import android.net.Uri
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.hestabit.sparkmatch.data.UserProfile
import com.hestabit.sparkmatch.router.AuthRoute
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(db: FirebaseFirestore, private val storage: FirebaseStorage) : UserRepository {

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
        val imageRef = storage.reference.child("profile_images/${UUID.randomUUID()}")
        return try {
            imageRef.putFile(imageUri).await()
            imageRef.downloadUrl.await().toString()
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun saveUserProfile(userId: String, userProfile: UserProfile): Result<Unit> {
        return try {
            val userdata = userProfile.copy(passions = passionsToStringList(userProfile.passionsObject), passionsObject = emptyList())
            usersCollection.document(userId).set(userdata).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(userId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            usersCollection.document(userId).set(updates, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val document = usersCollection.document(userId).get().await()
            if (document.exists()) {
                document.toObject(UserProfile::class.java)
            }else{
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}