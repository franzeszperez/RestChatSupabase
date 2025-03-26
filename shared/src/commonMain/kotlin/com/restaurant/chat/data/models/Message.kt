package com.restaurant.chat.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val channelId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val attachments: List<Attachment> = emptyList(),
    val isRead: Boolean = false,
    val isEdited: Boolean = false
)

@Serializable
data class Attachment(
    val id: String,
    val type: AttachmentType,
    val url: String,
    val name: String,
    val size: Long
)

@Serializable
enum class AttachmentType {
    IMAGE,
    FILE,
    AUDIO,
    VIDEO
} 