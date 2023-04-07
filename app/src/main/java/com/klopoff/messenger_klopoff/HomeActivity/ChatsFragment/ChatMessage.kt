package com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment

data class ChatMessage(
    val mine: Boolean,
    val message: String,
    val createdAt: Long
)