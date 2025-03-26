package com.restaurant.chat.data.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val restaurantId: String,
    val role: UserRole,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false,
    val lastSeen: Long? = null
)

@Serializable
enum class UserRole {
    ADMIN,
    MANAGER,
    EMPLOYEE
} 