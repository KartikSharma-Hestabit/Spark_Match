package com.hestabit.sparkmatch.repository

import com.hestabit.sparkmatch.data.Response
import com.hestabit.sparkmatch.data.UserProfile

interface DiscoverRepository {

    suspend fun fetchProfiles(): Response<List<UserProfile>>

}