package com.hestabit.sparkmatch.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hestabit.sparkmatch.utils.Utils.stringListToPassions
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.UserProfile
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DiscoverRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore, private val firebaseAuth: FirebaseAuth) :
    DiscoverRepository {

    override suspend fun fetchProfiles(): Response<List<UserProfile>> {
        return try {
            val resultList = firestore.collection("users").get().await()
                .documents.mapNotNull {
                    it.toObject(UserProfile::class.java)
                }

            resultList.forEach { doc ->
                doc.passionsObject = stringListToPassions(doc.passions)
            }

            Response.Success(resultList.filterNot { user -> user.uid ==  firebaseAuth.currentUser?.uid})
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }

    }

}