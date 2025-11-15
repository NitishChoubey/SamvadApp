package com.example.samvad.data.remote.websocket

import android.util.Log
import com.example.samvad.data.remote.dto.MessageDto
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.net.ssl.SSLSocketFactory

class WebSocketManager(
    private val serverUrl: String,
    private val token: String,
    private val onMessageReceived: (MessageDto) -> Unit
) {
    private var webSocketClient: WebSocketClient? = null
    private val gson = Gson()

    fun connect() {
        try {
            val uri = URI(serverUrl.replace("http", "ws") + "ws?token=$token")

            webSocketClient = object : WebSocketClient(uri) {
                override fun onOpen(handshakedata: ServerHandshake?) {
                    Log.d("WebSocket", "Connected to server")
                }

                override fun onMessage(message: String?) {
                    message?.let {
                        try {
                            val messageDto = gson.fromJson(it, MessageDto::class.java)
                            onMessageReceived(messageDto)
                        } catch (e: Exception) {
                            Log.e("WebSocket", "Error parsing message: ${e.message}")
                        }
                    }
                }

                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                    Log.d("WebSocket", "Connection closed: $reason")
                }

                override fun onError(ex: Exception?) {
                    Log.e("WebSocket", "WebSocket error: ${ex?.message}")
                }
            }

            webSocketClient?.connect()
        } catch (e: Exception) {
            Log.e("WebSocket", "Failed to connect: ${e.message}")
        }
    }

    fun sendMessage(message: MessageDto) {
        try {
            val jsonMessage = gson.toJson(message)
            webSocketClient?.send(jsonMessage)
        } catch (e: Exception) {
            Log.e("WebSocket", "Failed to send message: ${e.message}")
        }
    }

    fun disconnect() {
        webSocketClient?.close()
    }

    fun isConnected(): Boolean {
        return webSocketClient?.isOpen == true
    }
}

