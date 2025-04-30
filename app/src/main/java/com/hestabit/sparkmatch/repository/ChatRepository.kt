package com.hestabit.sparkmatch.repository

import com.hestabit.sparkmatch.data.ChatMessageItem
import com.hestabit.sparkmatch.data.ChatPreview
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getMessages(chatId: String): Flow<List<ChatMessageItem>>

    suspend fun sendMessage(chatId: String, message: ChatMessageItem)

    suspend fun updateChatPreview(chatId: String, userId: String, preview: ChatPreview)

    fun getChatPreviews(userId: String): Flow<List<ChatPreview>>

}