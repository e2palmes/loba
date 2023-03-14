package com.example.loba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.loba.models.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "PostsActivity"
class PostsActivity : AppCompatActivity() {

    val firestoreDb = Firebase.firestore
    private lateinit var posts : MutableList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

//        Create the layout file which  represents one post
//        Create data source
//        create the adapter
//        Bind 

        val postsReference = firestoreDb
            .collection("posts")
            .limit(20)
            .orderBy("created_at", Query.Direction.DESCENDING)

        postsReference.addSnapshotListener { snapshot, error ->

            if (error != null || snapshot == null) {
                Log.w(TAG, "Error getting documents", error)
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(Post::class.java)
            for (post in postList) {
                Log.i(TAG, "Post => $post")
            }
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