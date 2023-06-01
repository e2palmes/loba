package com.example.loba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.loba.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var registerButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var fullnameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fullnameEditText = findViewById(R.id.etFullname)
        emailEditText = findViewById(R.id.etEmail)
        usernameEditText = findViewById(R.id.etUsername)
        passwordEditText = findViewById(R.id.etPassword)
        confirmPasswordEditText = findViewById(R.id.etConfirm_Password)
        registerButton = findViewById(R.id.btnRegister)


        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener {

            val fullname = fullnameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirm_password = confirmPasswordEditText.text.toString().trim()

            // Validate user input

            if (email.isNotEmpty() && username.isNotEmpty()
                && fullname.isNotEmpty()
                && password.isNotEmpty()
                && confirm_password.isNotEmpty()

            ) {

                if( password.compareTo(confirm_password) != 0 )//test identical password
                {
                    Toast.makeText(
                        baseContext,
                        "Les 2 mots de passe ne se ressembblent pas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            // User registration successful, update UI with user information
                            val user = auth.currentUser

                            // Create a User object to store additional user information
                            val newUser = User(fullname, username, email)

                            // Store user information in Firestore Users collection
                            FirebaseFirestore.getInstance().collection("Users")
                                .document(user?.uid!!)
                                .set(newUser)
                                .addOnSuccessListener {
                                    // User information stored successfully, redirect to HomeActivity
                                    val intent = Intent(this, PostsActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener {
                                    // Failed to store user information in Firestore, delete user from Firebase Authentication
                                    user.delete()
                                        .addOnCompleteListener { deleteTask ->
                                            if (deleteTask.isSuccessful) {
                                                Toast.makeText(
                                                    baseContext,
                                                    "Failed to store user",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                }
                        } else { // User registration failed due to some error, display a message to the user

                            Toast.makeText(
                                baseContext,
                                "Registration Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }else{//end validate input
                Toast.makeText(this, "Entrer tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
    }
}