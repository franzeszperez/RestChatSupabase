package com.restaurant.chat.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    val id: String,
    val type: ChannelType,
    val name: String,
    val participants: List<String>, // User IDs
    val lastMessage: Message? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val isGroup: Boolean = false
)

@Serializable
enum class ChannelType {
    GENERAL,
    PRIVATE,
    GROUP
} 