package com.hestabit.sparkmatch.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firestore.admin.v1.Index
import com.hestabit.sparkmatch.Utils.convertMapToJsonString
import com.hestabit.sparkmatch.Utils.printDebug
import com.hestabit.sparkmatch.Utils.stringListToPassions
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.UserProfile
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DiscoverRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    DiscoverRepository {

    override suspend fun fetchProfiles(): Response<List<UserProfile>> {
        return try {
            val resultList = firestore.collection("users").get().await()
                .documents.mapNotNull { it.toObject(UserProfile::class.java) }

            resultList.forEach { doc ->
                doc.passionsObject = stringListToPassions(doc.passions)
            }

            Response.Success(resultList)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }

    }

}