package com.hestabit.sparkmatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hestabit.sparkmatch.data.Message
import com.hestabit.sparkmatch.repository.ChatRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepositoryImpl
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun loadMessages(chatId: String) {
        viewModelScope.launch {
            chatRepository.getMessages(chatId).collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(chatId: String, senderId: String, text: String) {
        if (text.isNotBlank()) {
            val message = Message(senderId = senderId, text = text)
            chatRepository.sendMessage(chatId, message)
        }
    }

    fun startChat(user1Id: String, user2Id: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val chatId = chatRepository.startChat(user1Id, user2Id)
            onResult(chatId)
            loadMessages(chatId)
        }
    }
}