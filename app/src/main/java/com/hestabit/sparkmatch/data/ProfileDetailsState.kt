package com.hestabit.sparkmatch.data

import android.net.Uri
import java.time.LocalDate

data class ProfileDetailsState(
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: LocalDate? = null,
    val profileImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
