package com.hestabit.sparkmatch.data

import com.hestabit.sparkmatch.R

data class ChatMessage(
    val senderName: String,
    val senderImage: Int,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int,
    val isOnline: Boolean,
    val story: Boolean
)

val sampleChats = listOf(
    ChatMessage("Emelie", R.drawable.emelie, "Sticker", "23 min", 1, true, true),
    ChatMessage("Abigail", R.drawable.abigail, "Typing...", "27min", 1, true, false),
    ChatMessage("Elizabeth", R.drawable.elizabeth, "Ok, see you then", "33 min", 0, true, true),
    ChatMessage("Penelope", R.drawable.penelope, "You: Hey! What’s up, long time..", "50 min", 0, false, false),
    ChatMessage("Chloe", R.drawable.chloe, "You: Hello how are you?", "55 min", 0, false, false),
    ChatMessage("Grace", R.drawable.img_4, "You: Great I will write later..", "1 hour", 0, false, true),
    ChatMessage("Emelie", R.drawable.emelie, "Sticker", "23 min", 1, true, true),
    ChatMessage("Abigail", R.drawable.abigail, "Typing...", "27min", 1, true, false),
    ChatMessage("Elizabeth", R.drawable.elizabeth, "Ok, see you then", "33 min", 0, true, true),
    ChatMessage("Penelope", R.drawable.penelope, "You: Hey! What’s up, long time..", "50 min", 0, false, false),
    ChatMessage("Chloe", R.drawable.chloe, "You: Hello how are you?", "55 min", 0, false, false),
    ChatMessage("Grace", R.drawable.img_4, "You: Great I will write later..", "1 hour", 0, false, true),
    )