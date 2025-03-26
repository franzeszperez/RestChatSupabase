package com.restaurant.chat.presentation

import androidx.compose.runtime.*
import com.restaurant.chat.data.models.Channel
import com.restaurant.chat.data.models.User
import com.restaurant.chat.presentation.screens.ChatListScreen
import com.restaurant.chat.presentation.screens.ChatRoomScreen
import com.restaurant.chat.presentation.screens.LoginScreen

sealed class Screen {
    object Login : Screen()
    object ChatList : Screen()
    data class ChatRoom(val channel: Channel) : Screen()
}

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }
    var currentUser by remember { mutableStateOf<User?>(null) }

    MaterialTheme {
        when (currentScreen) {
            is Screen.Login -> {
                LoginScreen(
                    onLoginSuccess = { user ->
                        currentUser = user
                        currentScreen = Screen.ChatList
                    },
                    onSignUpClick = {
                        // Handle sign up navigation
                    }
                )
            }
            is Screen.ChatList -> {
                currentUser?.let { user ->
                    ChatListScreen(
                        currentUser = user,
                        onChannelClick = { channel ->
                            currentScreen = Screen.ChatRoom(channel)
                        },
                        onSignOut = {
                            currentUser = null
                            currentScreen = Screen.Login
                        }
                    )
                }
            }
            is Screen.ChatRoom -> {
                currentUser?.let { user ->
                    ChatRoomScreen(
                        channel = (currentScreen as Screen.ChatRoom).channel,
                        currentUser = user,
                        onBackClick = {
                            currentScreen = Screen.ChatList
                        }
                    )
                }
            }
        }
    }
} 