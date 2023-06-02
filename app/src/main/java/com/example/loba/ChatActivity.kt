package com.example.loba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.example.loba.adapters.MessagesAdapter
import com.example.loba.models.Message
import com.example.loba.models.Post
import com.example.loba.models.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity : AppCompatActivity() {
    private lateinit var messageListView: ListView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button

    private lateinit var messageAdapter: MessagesAdapter
    private lateinit var messageList: MutableList<Message>

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var currentUserId: String
    private lateinit var receiverUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)




        // Initialize Firebase components
        database = FirebaseDatabase.getInstance("https://fitshare-32c7a-default-rtdb.europe-west1.firebasedatabase.app").reference
        auth = FirebaseAuth.getInstance()
        currentUserId = auth.currentUser?.uid ?: ""
        receiverUserId = intent.getStringExtra("userId") ?: ""

        // Initialize views
        messageListView = findViewById(R.id.lvMessages)
        messageEditText = findViewById(R.id.etMessage)
        sendButton = findViewById(R.id.btnSend)

        // Initialize message list
        messageList = mutableListOf()
        messageAdapter = MessagesAdapter(this, messageList)
        messageListView.adapter = messageAdapter

        // Send button click listener
        sendButton.setOnClickListener {
            sendMessage()
        }

        // Read messages from the database
        database.child("messages").child(getChatId()).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val message = dataSnapshot.getValue(Message::class.java)
                if (message != null) {
                    messageList.add(message)
                    messageAdapter.notifyDataSetChanged()
                    scrollToBottom()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Handle when a child message is changed
                val updatedMessage = snapshot.getValue(Message::class.java)
                val messageIndex = messageList.indexOfFirst { it.createdAt == updatedMessage?.createdAt }
                if (messageIndex != -1 && updatedMessage != null) {
                    messageList[messageIndex] = updatedMessage
                    messageAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Handle when a child message is removed
                val removedMessage = snapshot.getValue(Message::class.java)
                val messageIndex = messageList.indexOfFirst { it.createdAt == removedMessage?.createdAt }
                if (messageIndex != -1) {
                    messageList.removeAt(messageIndex)
                    messageAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun sendMessage() {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("Users")

        val messageText = messageEditText.text.toString().trim()
        if (TextUtils.isEmpty(messageText)) {
            return
        }

        usersCollection.document(currentUserId).get()
            .addOnSuccessListener { userDocument ->
                if (userDocument.exists()) {
                    val user = userDocument.toObject(User::class.java)
                    if (user != null) {
                        val message = Message(user.fullname, messageText, Timestamp.now().toDate().time)
                        database.child("messages").child(getChatId()).push().setValue(message)
                        messageEditText.text.clear()

                    }
                }
            }


    }

    private fun scrollToBottom() {
        messageListView.post {
            messageListView.setSelection(messageAdapter.count - 1)
        }
    }

    private fun getChatId(): String {
        // Generate a unique chat ID based on the user IDs
        val sortedUserIds = listOf(currentUserId, receiverUserId).sorted()
        return "${sortedUserIds[0]}_${sortedUserIds[1]}"
    }
}