package com.restaurant.chat.data.repositories

import com.restaurant.chat.data.SupabaseClient
import com.restaurant.chat.data.models.User
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository {
    private val client = SupabaseClient.client

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val auth = client.auth.signInWith(email, password)
            val user = client.postgrest["users"].select {
                eq("id", auth.user?.id)
            }.decodeSingle<User>()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String, name: String, restaurantId: String): Result<User> {
        return try {
            val auth = client.auth.signUpWith(email, password)
            val user = User(
                id = auth.user?.id ?: throw Exception("User ID is null"),
                email = email,
                name = name,
                restaurantId = restaurantId,
                role = UserRole.EMPLOYEE
            )
            client.postgrest["users"].insert(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        client.auth.signOut()
    }

    fun getCurrentUser(): Flow<User?> {
        return client.auth.currentUserOrNull().map { user ->
            if (user != null) {
                client.postgrest["users"].select {
                    eq("id", user.id)
                }.decodeSingle<User>()
            } else null
        }
    }
} 