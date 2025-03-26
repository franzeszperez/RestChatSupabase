package com.restaurant.chat.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.restaurant.chat.data.models.Channel
import com.restaurant.chat.data.models.User
import com.restaurant.chat.data.repositories.AuthRepository
import com.restaurant.chat.data.repositories.ChatRepository
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    currentUser: User,
    onChannelClick: (Channel) -> Unit,
    onSignOut: () -> Unit
) {
    val chatRepository = remember { ChatRepository() }
    val authRepository = remember { AuthRepository() }
    var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }
    var showNewChatDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser.id) {
        chatRepository.getChannels(currentUser.id).collectLatest { channelList ->
            channels = channelList
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chats") },
                actions = {
                    IconButton(onClick = { showNewChatDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "New Chat")
                    }
                    IconButton(onClick = {
                        authRepository.signOut()
                        onSignOut()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Sign Out")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(channels) { channel ->
                ChatListItem(
                    channel = channel,
                    onClick = { onChannelClick(channel) }
                )
            }
        }

        if (showNewChatDialog) {
            NewChatDialog(
                onDismiss = { showNewChatDialog = false },
                onConfirm = { name, isGroup ->
                    // Handle new chat creation
                    showNewChatDialog = false
                }
            )
        }
    }
}

@Composable
fun ChatListItem(
    channel: Channel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = channel.name,
                    style = MaterialTheme.typography.titleMedium
                )
                channel.lastMessage?.let { message ->
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            channel.lastMessage?.let { message ->
                Text(
                    text = formatTimestamp(message.timestamp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun NewChatDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var isGroup by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Chat") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Chat Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isGroup,
                        onCheckedChange = { isGroup = it }
                    )
                    Text("Group Chat")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name, isGroup) },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun formatTimestamp(timestamp: Long): String {
    // Implement timestamp formatting logic
    return "Just now" // Placeholder
} 