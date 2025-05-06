package com.hestabit.sparkmatch.data

data class MatchUser(
    val profileImageUrl: String = "",
    val uid: String = "",
    val firstName: String = "",
    val birthday: String = "",
    val message: Message? = null,
    val chatId: String = ""
)