package com.example.samvad.presentation.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samvad.BuildConfig
import com.example.samvad.data.local.TokenManager
import com.example.samvad.data.remote.api.MessageApi
import com.example.samvad.data.remote.api.UserApi
import com.example.samvad.data.remote.dto.MessageDto
import com.example.samvad.data.remote.websocket.WebSocketManager
import com.example.samvad.models.Message
import com.example.samvad.presentation.homescreen.ChatDesignModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val userApi: UserApi,
    private val messageApi: MessageApi
) : ViewModel() {

    private var webSocketManager: WebSocketManager? = null
    private val messageCallbacks = mutableMapOf<String, (Message) -> Unit>()

    private val _chatList = MutableStateFlow<List<ChatDesignModel>>(emptyList())
    val chatList = _chatList.asStateFlow()

    init {
        initializeWebSocket()
        loadChatData()
    }

    private fun initializeWebSocket() {
        val token = tokenManager.getToken()
        if (token != null) {
            webSocketManager = WebSocketManager(
                serverUrl = BuildConfig.BASE_URL,
                token = token,
                onMessageReceived = { message ->
                    handleIncomingMessage(message)
                }
            )
            webSocketManager?.connect()
        }
    }

    private fun handleIncomingMessage(messageDto: MessageDto) {
        val message = Message(
            senderPhoneNumber = messageDto.senderId,
            message = messageDto.messageContent,
            timeStamp = messageDto.timestamp
        )

        // Notify callbacks
        val conversationKey = "${messageDto.senderId}-${messageDto.receiverId}"
        messageCallbacks[conversationKey]?.invoke(message)

        Log.d("BaseViewModel", "Received message: ${message.message}")
    }

    fun getUserId(): String? = tokenManager.getUserId()

    fun getPhoneNumber(): String? = tokenManager.getPhoneNumber()

    fun searchUserByPhoneNumber(phoneNumber: String, callback: (ChatDesignModel?) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("BaseViewModel", "Searching for user with phone: $phoneNumber")

                val response = userApi.searchByPhoneNumber(phoneNumber)
                if (response.isSuccessful && response.body() != null) {
                    val userDto = response.body()!!
                    Log.d("BaseViewModel", "User found: ${userDto.userId}, ${userDto.name}")

                    val chatModel = ChatDesignModel(
                        userId = userDto.userId,
                        phoneNumber = userDto.phoneNumber,
                        name = userDto.name.ifEmpty { userDto.phoneNumber },
                        message = "",
                        time = "",
                        image = null
                    )
                    callback(chatModel)
                } else {
                    Log.e("BaseViewModel", "User not found: ${response.code()}")
                    callback(null)
                }
            } catch (e: Exception) {
                Log.e("BaseViewModel", "Error searching user: ${e.message}", e)
                callback(null)
            }
        }
    }

    fun getChatForUser(userId: String, callback: (List<ChatDesignModel>) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("BaseViewModel", "Getting chats for user: $userId")
                // TODO: Implement get chats API endpoint in backend
                callback(emptyList())
            } catch (e: Exception) {
                Log.e("BaseViewModel", "Error fetching user chats: ${e.message}")
                callback(emptyList())
            }
        }
    }

    private fun loadChatData() {
        viewModelScope.launch {
            try {
                val currentUserId = tokenManager.getUserId()
                if (currentUserId != null) {
                    Log.d("BaseViewModel", "Loading chat data for user: $currentUserId")
                    // TODO: Implement get chats list API endpoint in backend
                    _chatList.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("BaseViewModel", "Error loading chat data: ${e.message}")
            }
        }
    }

    fun addChat(newChat: ChatDesignModel) {
        viewModelScope.launch {
            try {
                val currentUserId = tokenManager.getUserId()
                if (currentUserId != null) {
                    Log.d("BaseViewModel", "Chat added successfully")

                    // TODO: Implement add chat API endpoint in backend

                    // Update local list
                    _chatList.value = _chatList.value + newChat
                } else {
                    Log.e("BaseViewModel", "No user is authenticated")
                }
            } catch (e: Exception) {
                Log.e("BaseViewModel", "Failed to add chat: ${e.message}")
            }
        }
    }

    fun sendMessage(senderPhoneNumber: String, receiverPhoneNumber: String, messageText: String) {
        viewModelScope.launch {
            try {
                val senderId = tokenManager.getUserId() ?: return@launch

                val messageDto = MessageDto(
                    senderId = senderId,
                    receiverId = receiverPhoneNumber, // TODO: Convert phone to userId
                    messageContent = messageText,
                    timestamp = System.currentTimeMillis(),
                    messageType = "TEXT"
                )

                // Send via WebSocket
                webSocketManager?.sendMessage(messageDto)

                Log.d("BaseViewModel", "Message sent successfully")
            } catch (e: Exception) {
                Log.e("BaseViewModel", "Failed to send message: ${e.message}")
            }
        }
    }

    fun getMessage(
        senderPhoneNumber: String,
        receiverPhoneNumber: String,
        onNewMessage: (Message) -> Unit
    ) {
        // Store callback for this conversation
        val conversationKey = "$senderPhoneNumber-$receiverPhoneNumber"
        messageCallbacks[conversationKey] = onNewMessage

        // Load message history from API
        viewModelScope.launch {
            try {
                // TODO: Implement get conversation API call
                // val response = messageApi.getConversation(receiverId)
                // if (response.isSuccessful && response.body() != null) {
                //     response.body()!!.forEach { messageDto ->
                //         val message = Message(
                //             senderPhoneNumber = messageDto.senderId,
                //             message = messageDto.messageContent,
                //             timeStamp = messageDto.timestamp
                //         )
                //         onNewMessage(message)
                //     }
                // }

                Log.d("BaseViewModel", "Getting messages for conversation: $conversationKey")
            } catch (e: Exception) {
                Log.e("BaseViewModel", "Error getting messages: ${e.message}")
            }
        }
    }

    fun fetchLastMessageForChat(
        sendersPhoneNumber: String,
        receiversPhoneNumber: String,
        onLastMessageFetched: (String, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("BaseViewModel", "Fetching last message for chat")
                // TODO: Implement get last message API endpoint
                onLastMessageFetched("No message", "--:--")
            } catch (e: Exception) {
                Log.e("BaseViewModel", "Error fetching last message: ${e.message}")
                onLastMessageFetched("No message", "--:--")
            }
        }
    }

    fun loadChatList(
        currentUserPhoneNumber: String,
        onChatListLoaded: (List<ChatDesignModel>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d("BaseViewModel", "Loading chat list for: $currentUserPhoneNumber")
                // TODO: Implement get chat list API endpoint
                onChatListLoaded(emptyList())
            } catch (e: Exception) {
                Log.e("BaseViewModel", "Error loading chat list: ${e.message}")
                onChatListLoaded(emptyList())
            }
        }
    }

    private fun decodeBase64ToBitmap(base64Image: String): Bitmap? {
        return try {
            val decodedByte = Base64.decode(base64Image, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
        } catch (e: IOException) {
            null
        }
    }

    fun base64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedByte = Base64.decode(base64String, Base64.DEFAULT)
            val inputStream: InputStream = ByteArrayInputStream(decodedByte)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            null
        }
    }

    fun convertBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val inputStream: InputStream = ByteArrayInputStream(decodedBytes)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            null
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager?.disconnect()
        messageCallbacks.clear()
    }
}

