package com.example.loba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loba.databinding.ActivityEditProfileBinding
import com.example.loba.databinding.ActivityPostsBinding
import com.example.loba.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var editName: EditText
    private lateinit var email: EditText
    private lateinit var username: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editName = binding.etEditName
        email = binding.etEmail
        username = binding.etUsername
        btnSave = binding.btnSave

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { fetchUserData(it) }
    }

    private fun fetchUserData(userId: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    editName.setText(user.fullname)
                    email.setText(user.email)
                    username.setText(user.username)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Gérer l'erreur de récupération des données
            }
        })
    }
}
