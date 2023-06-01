package com.example.loba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import com.example.loba.adapters.ContactsAdapter
import com.example.loba.adapters.PostsAdapter
import com.example.loba.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ContactsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("Users")

        val contactsListView: ListView = findViewById(R.id.lvContacts)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        usersCollection.get()
            .addOnSuccessListener { querySnapshot ->
                // List to store the retrieved users
                val userList = mutableListOf<User>()

                for (document in querySnapshot) {
                    // Retrieve user details from document fields
                    val userId = document.id

                    val username = document.getString("username")
                    val fullname = document.getString("fullname")
                    val email = document.getString("email")
                    val profilePic = document.getString("profile_picture")

                    // Create a User object
                    if(fullname !=null &&
                        username !=null &&
                        email !=null &&
                        currentUser != null
                        && userId != currentUser.uid){

                        val user = User(userId,fullname, username, email )
                        userList.add(user)
                    }

                }
                val contactsAdapter = ContactsAdapter(this, userList)
                contactsListView.adapter = contactsAdapter

                // Set item click listener
                contactsListView.setOnItemClickListener { _, _, position, _ ->
                    val selectedUser = userList[position]
                    selectedUser.id?.let { openChatActivity(it) }
                }

            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error retrieving contacs: ${exception.message}")

            }

    }
    private fun openChatActivity(userId:String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("userId",userId)

        startActivity(intent)
    }
}