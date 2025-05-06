package com.hestabit.sparkmatch.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hestabit.sparkmatch.data.Message
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatRepository {

    override fun getMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val listener = firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull {
                    it.toObject(Message::class.java)
                } ?: emptyList()

                trySend(messages)
            }

        awaitClose { listener.remove() }
    }

    override fun sendMessage(chatId: String, message: Message) {
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
    }

    override suspend fun startChat(user1Id: String, user2Id: String): String {
        val chatId = listOf(user1Id, user2Id).sorted().joinToString("_")
        val chatRef = firestore.collection("chats").document(chatId)

        val chatData = mapOf(
            "participants" to listOf(user1Id, user2Id),
            "createdAt" to System.currentTimeMillis()
        )

        chatRef.set(chatData).await()
        return chatId
    }
}