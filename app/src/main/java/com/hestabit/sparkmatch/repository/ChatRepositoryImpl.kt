package com.hestabit.sparkmatch.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hestabit.sparkmatch.data.ChatMessageItem
import com.hestabit.sparkmatch.data.ChatPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor (
    private val db: FirebaseFirestore
) : ChatRepository {

    override fun getMessages(chatId: String): Flow<List<ChatMessageItem>> = callbackFlow {
        val listener = db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    close(error ?: Exception("Snapshot error"))
                    return@addSnapshotListener
                }

                val messages = snapshot.documents.mapNotNull { it.toObject(ChatMessageItem::class.java) }
                trySend(messages)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun sendMessage(chatId: String, message: ChatMessageItem) {
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
            .await()
    }

    override suspend fun updateChatPreview(chatId: String, userId: String, preview: ChatPreview) {
        db.collection("users")
            .document(userId)
            .collection("chatPreviews")
            .document(chatId)
            .set(preview)
            .await()
    }

    override fun getChatPreviews(userId: String): Flow<List<ChatPreview>> = callbackFlow {
        val listener = db.collection("users")
            .document(userId)
            .collection("chatPreviews")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    close(error ?: Exception("Snapshot error"))
                    return@addSnapshotListener
                }

                val previews = snapshot.documents.mapNotNull { it.toObject(ChatPreview::class.java) }
                trySend(previews)
            }

        awaitClose { listener.remove() }
    }
}