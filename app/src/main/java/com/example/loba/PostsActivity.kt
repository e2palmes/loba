package com.example.loba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "PostsActivity"
class PostsActivity : AppCompatActivity() {

    val firestoreDb = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        val postsReference = firestoreDb.collection("posts")
        postsReference.get().addOnSuccessListener { result ->
            for (document in result){
                Log.i(TAG, "${document.id} => ${document.data}")
            }
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents", exception)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile){
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}