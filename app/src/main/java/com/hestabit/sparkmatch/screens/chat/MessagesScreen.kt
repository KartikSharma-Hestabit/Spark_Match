package com.hestabit.sparkmatch.screens.chat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hestabit.sparkmatch.utils.Utils.generateChatId
import com.hestabit.sparkmatch.viewmodel.ChatViewModel

@Composable
fun MessagesScreen(onNavigate: (String) -> Unit) {
    val viewModel: ChatViewModel = hiltViewModel()
    val onChatClick: (chatId: String, recipientId: String, name: String, image: Int) -> Unit
    val chatPreviews by viewModel.chatPreviews.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadChatPreviews()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(chatPreviews) { chat ->
            MessageCell(
                senderName = chat.senderName,
                senderId = chat.senderId,
                senderImage = chat.senderImage,
                lastMessage = chat.lastMessage,
                timestamp = chat.timestamp,
                unreadCount = chat.unreadCount,
                isOnline = chat.isOnline,
                story = chat.story,
                onClick = {
                    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return@MessageCell
                    val chatId = generateChatId(currentUserId, chat.senderId)
                    onChatClick(chatId, chat.senderId, chat.senderName, chat.senderImage)
                }
            )
        }
    }
}