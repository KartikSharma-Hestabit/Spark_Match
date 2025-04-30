package com.hestabit.sparkmatch.data

data class ChatMessageItem(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)