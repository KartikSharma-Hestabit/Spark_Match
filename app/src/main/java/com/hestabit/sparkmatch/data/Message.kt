package com.hestabit.sparkmatch.data

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String? = null,
    val unreadText: Int = 0
)