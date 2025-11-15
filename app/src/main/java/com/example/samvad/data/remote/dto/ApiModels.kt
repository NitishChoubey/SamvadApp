package com.example.samvad.data.remote.dto

data class SendOtpRequest(
    val phoneNumber: String
)

data class SendOtpResponse(
    val message: String,
    val phoneNumber: String
)

data class VerifyOtpRequest(
    val phoneNumber: String,
    val otp: String
)

data class AuthResponse(
    val token: String,
    val refreshToken: String,
    val user: UserDto
)

data class UserDto(
    val userId: String,
    val phoneNumber: String,
    val name: String,
    val status: String,
    val profileImageUrl: String?
)

data class UpdateProfileRequest(
    val name: String,
    val status: String
)

data class MessageDto(
    val messageId: String? = null,
    val senderId: String,
    val receiverId: String,
    val messageContent: String,
    val timestamp: Long,
    val messageType: String = "TEXT",
    val isDelivered: Boolean = false,
    val isRead: Boolean = false
)

data class ErrorResponse(
    val error: String
)

