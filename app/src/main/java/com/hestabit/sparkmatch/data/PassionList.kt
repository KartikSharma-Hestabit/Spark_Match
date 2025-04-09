package com.hestabit.sparkmatch.data

import com.hestabit.sparkmatch.router.AuthRoute

data class PassionList(
    val name: String,
    val iconRes: Int,
    var isSelected: Boolean = false,
    val passionType: AuthRoute.PassionType? = AuthRoute.PassionType.fromTitle(name)
)
