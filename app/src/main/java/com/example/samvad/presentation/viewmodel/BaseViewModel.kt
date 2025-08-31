package com.example.samvad.presentation.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import com.example.samvad.models.Message
import com.example.samvad.presentation.homescreen.ChatDesignModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream




class BaseViewModel : ViewModel() {

    fun searchUserByPhoneNumber(phoneNumber: String, callback: (ChatDesignModel?) -> Unit) {

        val currentUser = FirebaseAuth.getInstance()

        if (currentUser == null) {
            android.util.Log.e("BaseViewModel", "User is not authenticated")
            callback(null)
            return
        }

        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.orderByChild("phoneNumber").equalTo(phoneNumber)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val user = snapshot.children.first().getValue(ChatDesignModel::class.java)
                        callback(user)
                    } else {
                        callback(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    android.util.Log.e(
                        "BaseViewModel",
                        "Error fetching User: ${error.message}, Details: ${error.details}"
                    )
                    callback(null)
                }

            })
    }

    fun getChatForUser(userId: String, callback: (List<ChatDesignModel>) -> Unit) {

        val chatRef = FirebaseDatabase.getInstance().getReference("users/$userId/chats")
        chatRef.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val chatList = mutableListOf<ChatDesignModel>()

                    for (childSnapshot in snapshot.children) {

                        val chat = childSnapshot.getValue(ChatDesignModel::class.java)

                        if (chat != null) {
                            chatList.add(chat)
                        }
                    }
                    callback(chatList)
                }

                override fun onCancelled(error: DatabaseError) {
                    android.util.Log.e(
                        "BaseViewModel",
                        "Error fetching user chats : ${error.message}"
                    )
                    callback(emptyList())
                }
            })
    }

    private val _chatList = MutableStateFlow<List<ChatDesignModel>>(emptyList())
    val chatList = _chatList.asStateFlow()

    init {
        loadChatData()
    }

    private fun loadChatData() {

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        if (currentUserId != null) {

            val chatRef = FirebaseDatabase.getInstance().getReference("chats")

            chatRef.orderByChild("userId").equalTo(currentUserId)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val chatList = mutableListOf<ChatDesignModel>()
                        for (childSnapshot in snapshot.children) {

                            val chat = childSnapshot.getValue(ChatDesignModel::class.java)

                            if (chat != null) {

                                chatList.add(chat)
                            }
                        }
                        _chatList.value = chatList
                    }

                    override fun onCancelled(error: DatabaseError) {
                        android.util.Log.e(
                            "BaseViewModel",
                            "Error fetching user chats : ${error.message}"
                        )
                    }

                })
        }
    }

    fun addChat(newChat: ChatDesignModel) {

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {

            val newChatRef = FirebaseDatabase.getInstance().getReference("chats").push()
            val chatWithUser = newChat.copy(currentUserId)
            newChatRef.setValue(chatWithUser).addOnSuccessListener {

                android.util.Log.d("BaseViewModel", "chat added successfully to firebase")
            }.addOnFailureListener { exception ->
                android.util.Log.e("BaseViewModel", "Failed to add chat: ${exception.message}")
            }

        } else {
            android.util.Log.e("BaseViewModel", "No user is authenticated")
        }
    }

    private val dataBaseReference = FirebaseDatabase.getInstance().reference

    fun sendMessage(senderPhoneNumber: String, receiverPhoneNumber: String, messageText: String) {

        val messageId = dataBaseReference.push().key ?: return

        val message = Message(
            senderPhoneNumber = senderPhoneNumber,
            message = messageText,
            timeStamp = System.currentTimeMillis()
        )

        dataBaseReference.child("messages")
            .child(senderPhoneNumber)
            .child(receiverPhoneNumber)
            .child(messageId)
            .setValue(message)

        dataBaseReference.child("messages")
            .child(receiverPhoneNumber)
            .child(senderPhoneNumber)
            .child(messageId)
            .setValue(message)


    }

    fun getMessage(
        senderPhoneNumber: String,
        receiverPhoneNumber: String,
        onNewMessage: (Message) -> Unit
    ) {
        val messageRef =
            dataBaseReference.child("messages").child(senderPhoneNumber).child(receiverPhoneNumber)

        messageRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, presentChildName: String?) {

                val message = snapshot.getValue(Message::class.java)

                if (message != null) {
                    onNewMessage(message)


                }
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


    }

    fun fetchLastMessageForChat(
        sendersPhoneNumber: String,
        receiversPhoneNumber: String,
        onLastMessageFetched: (String, String) -> Unit
    ) {
        val chatRef =
            FirebaseDatabase.getInstance().reference.child("messages").child(sendersPhoneNumber)
                .child(receiversPhoneNumber)

        chatRef.orderByChild("timestamp").limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val lastMessage =
                            snapshot.children.firstOrNull()?.child("message")?.value as? String
                        val timestamp =
                            snapshot.children.firstOrNull()?.child("message")?.value as? String
                        onLastMessageFetched(lastMessage ?: "No message", timestamp ?: "--:--")
                    } else {

                        onLastMessageFetched("No message", "--:--")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onLastMessageFetched("No message", "--:--")
                }
            })
    }

    fun loadChatList(
        currentUserPhoneNumber: String,
        onChatListLoaded: (List<ChatDesignModel>) -> Unit

    ) {

        val chatList = mutableListOf<ChatDesignModel>()
        val chatRef =
            FirebaseDatabase.getInstance().reference.child("chats").child(currentUserPhoneNumber)


        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.children.forEach { child ->
                        val phoneNumber = child.key ?: return@forEach
                        val name = child.child("name").value as? String ?: "Unknown"
                        val image = child.child("image").value as? String

                        val profileImageBitmap = image?.let { decodeBase64ToBitmap(it) }

                        fetchLastMessageForChat(currentUserPhoneNumber, phoneNumber) { lastMessage, time ->
                            chatList.add(
                                ChatDesignModel(
                                    name = name,
                                    image = profileImageBitmap as Int?,
                                    message = lastMessage,
                                    time = time

                                )
                            )

                            if(chatList.size == snapshot.childrenCount.toInt()){
                                onChatListLoaded(chatList)
                            }


                        }

                    }
                }else{
                    onChatListLoaded(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onChatListLoaded(emptyList())
            }
            })

        }


    private fun decodeBase64ToBitmap(base64Image: String): Bitmap?{

        return try{
            val decodedByte = Base64.decode(base64Image , Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedByte , 0 , decodedByte.size)
        }catch(e : IOException){
            null
        }

    }

    fun base64ToBitmap(base64String: String): Bitmap? {
        return try{
            val decodedByte = Base64.decode(base64String , Base64.DEFAULT)
            val inputStream : InputStream = ByteArrayInputStream(decodedByte)
            BitmapFactory.decodeStream(inputStream)
        }catch (e : IOException){
            null
        }
    }

    }

