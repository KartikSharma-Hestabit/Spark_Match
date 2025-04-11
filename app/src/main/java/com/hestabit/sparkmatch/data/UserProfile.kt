package com.hestabit.sparkmatch.data

import android.net.Uri
import com.hestabit.sparkmatch.router.AuthRoute

data class UserProfile(
    val firstName: String = "",
    val lastName: String = "",
    val profileImageUrl: String? = null,
    val birthday: String = "",
    val gender: String = "",
    val interestPreference: String = "Everyone",
    val location: String = "",
    val profession: String = "",
    val about: String = "",
    val passions: List<String> = emptyList(),
    var passionsObject: List<AuthRoute.PassionType> = emptyList()
){
    val profileImage: Uri? get() = profileImageUrl?.let { Uri.parse(it) }
}