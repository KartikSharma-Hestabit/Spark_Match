package com.hestabit.sparkmatch.data

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.hestabit.sparkmatch.router.AuthRoute
import com.hestabit.sparkmatch.utils.Utils
import androidx.core.net.toUri

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
    var passionsObject: List<AuthRoute.PassionType> = emptyList(),
    val galleryImages: List<String> = emptyList()
) {
    val profileImage: Uri? get() = profileImageUrl?.toUri()
    val displayName: String get() = "$firstName $lastName"
    val age: String @RequiresApi(Build.VERSION_CODES.O)
    get() = if (birthday.isNotEmpty()) {
        try {
            Utils.getAgeFromBirthday(birthday)
        } catch (e: Exception) {
            ""
        }
    } else ""
}