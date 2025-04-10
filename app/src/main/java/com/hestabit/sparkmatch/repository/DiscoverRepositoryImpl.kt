package com.hestabit.sparkmatch.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.UserProfile
import javax.inject.Inject

class DiscoverRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) : DiscoverRepository {

    override fun fetchProfiles(): Response<List<UserProfile>> {
        TODO("Not yet implemented")
    }

}