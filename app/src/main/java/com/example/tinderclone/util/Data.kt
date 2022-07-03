package com.example.tinderclone.util

// data class which helps in storing the data of users
data class User(
    val uid: String? = "",
    val name: String? = "",
    val age: String? = "",
    val email: String? = "",
    val gender: String? = "",
    val preferredGender: String? = "",
    val imageUrl: String? = ""
)

// data class which helps in storing the data of chats screen
data class Chat(
    val userId: String? = "",
    val chatId: String? = "",
    val otherUserId: String? = "",
    val otherUserName: String? = "",
    val otherUserImageUrl: String? = ""
)

// data class for messages
data class Message(
    val sentBy: String? = null,
    val message: String? = null,
    val time: String? = null
)