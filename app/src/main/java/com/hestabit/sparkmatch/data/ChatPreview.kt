package com.hestabit.sparkmatch.data

data class ChatPreview(
    val senderName: String = "",
    val senderId: String = "",
    val senderImage: Int = 0,
    val lastMessage: String = "",
    val timestamp: String = "",
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val story: Boolean = false
)