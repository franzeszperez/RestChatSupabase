package com.restaurant.chat.data.repositories

import com.restaurant.chat.data.SupabaseClient
import com.restaurant.chat.data.models.Channel
import com.restaurant.chat.data.models.Message
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepository {
    private val client = SupabaseClient.client

    suspend fun createChannel(name: String, participants: List<String>, isGroup: Boolean = false): Result<Channel> {
        return try {
            val channel = Channel(
                id = generateChannelId(),
                type = if (isGroup) ChannelType.GROUP else ChannelType.PRIVATE,
                name = name,
                participants = participants,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                isGroup = isGroup
            )
            client.postgrest["channels"].insert(channel)
            Result.success(channel)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMessage(channelId: String, content: String, senderId: String): Result<Message> {
        return try {
            val message = Message(
                id = generateMessageId(),
                channelId = channelId,
                senderId = senderId,
                content = content,
                timestamp = System.currentTimeMillis()
            )
            client.postgrest["messages"].insert(message)
            updateChannelLastMessage(channelId, message)
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getChannels(userId: String): Flow<List<Channel>> {
        return client.postgrest["channels"].select {
            contains("participants", listOf(userId))
        }.decodeFlow<List<Channel>>()
    }

    fun getMessages(channelId: String): Flow<List<Message>> {
        return client.postgrest["messages"].select {
            eq("channelId", channelId)
            order("timestamp", ascending = true)
        }.decodeFlow<List<Message>>()
    }

    private suspend fun updateChannelLastMessage(channelId: String, message: Message) {
        client.postgrest["channels"].update({
            set("lastMessage", message)
            set("updatedAt", System.currentTimeMillis())
        }) {
            eq("id", channelId)
        }
    }

    private fun generateChannelId(): String = java.util.UUID.randomUUID().toString()
    private fun generateMessageId(): String = java.util.UUID.randomUUID().toString()
} 