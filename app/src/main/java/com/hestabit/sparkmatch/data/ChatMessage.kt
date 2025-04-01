package com.hestabit.sparkmatch.data

data class ChatMessage(
    val senderName: String,
    val senderImage: Int,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int,
    val isOnline: Boolean,
    val story: Boolean
)
