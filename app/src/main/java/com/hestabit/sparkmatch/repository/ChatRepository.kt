package com.hestabit.sparkmatch.repository

import com.hestabit.sparkmatch.data.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(chatId: String): Flow<List<Message>>
    fun sendMessage(chatId: String, message: Message)
    suspend fun startChat(user1Id: String, user2Id: String): String
}