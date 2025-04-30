package com.hestabit.sparkmatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.hestabit.sparkmatch.data.ChatMessageItem
import com.hestabit.sparkmatch.data.ChatPreview
import com.hestabit.sparkmatch.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessageItem>>(emptyList())
    val messages: MutableStateFlow<List<ChatMessageItem>> = _messages

    private val _chatPreviews = MutableStateFlow<List<ChatPreview>>(emptyList())
    val chatPreviews: MutableStateFlow<List<ChatPreview>> = _chatPreviews

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            repository.getMessages(chatId).collectLatest {
                _messages.value = it
            }
        }
    }

    fun sendMessage(chatId: String, text: String, recipientId: String, recipientName: String, recipientImage: Int) {
        val senderId = auth.currentUser?.uid ?: return
        val message = ChatMessageItem(senderId = senderId, text = text)

        viewModelScope.launch {
            repository.sendMessage(chatId, message)

            val timestamp = System.currentTimeMillis().toString()
            val preview = ChatPreview(
                senderName = recipientName,
                senderImage = recipientImage,
                lastMessage = text,
                timestamp = timestamp,
                unreadCount = 1,
                isOnline = true,
                story = false
            )

            repository.updateChatPreview(chatId, senderId, preview)
            repository.updateChatPreview(chatId, recipientId, preview.copy(unreadCount = 0)) // reset sender's unread
        }
    }

    fun loadChatPreviews() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.getChatPreviews(userId).collectLatest {
                _chatPreviews.value = it
            }
        }
    }
}