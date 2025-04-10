package com.hestabit.sparkmatch.data

import android.net.Uri
import com.hestabit.sparkmatch.router.AuthRoute

data class UserProfile(
    val firstName: String,
    val lastName: String,
    val profileImage: Uri?,
    val birthday: String,
    val gender: String,
    val interestPreference: String = "Everyone",
    val passions: List<AuthRoute.PassionType>
)