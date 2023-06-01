package com.example.loba


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loba.adapters.PostsAdapter
import com.example.loba.databinding.ActivityPostsBinding
import com.example.loba.models.Post
import com.example.loba.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "PostsActivity"
private const val EXTRA_USERNAME = "EXTRA_USERNAME"
open class PostsActivity : AppCompatActivity() {

    private var signedInUser: User? = null
    val firestoreDb = Firebase.firestore
    private lateinit var posts: MutableList<Post>
    private lateinit var adapter: PostsAdapter
    private lateinit var binding: ActivityPostsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPosts.layoutManager = LinearLayoutManager(this)

        val recyclerView: RecyclerView = findViewById(R.id.rvPosts)
        val newPostButton: FloatingActionButton = findViewById(R.id.newPost)
        newPostButton.setOnClickListener {

            val intent = Intent(this, NewPostActivity::class.java)
            startActivity(intent)
        }

        val db = FirebaseFirestore.getInstance()
        val postsCollection = db.collection("Posts")
        val usersCollection = db.collection("Users")

        postsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val postList = mutableListOf<Post>()

                for (document in querySnapshot) {
                    val description = document.getString("description")
                    val image_url = document.getString("image_url")
                    val user = document.getString("user")
                    val createdAt = document.getTimestamp("createdAt")

                    if (description != null && createdAt != null) {
                        usersCollection.document(user.toString()).get()
                            .addOnSuccessListener { userDocument ->
                                if (userDocument.exists()) {
                                    val user = userDocument.toObject(User::class.java)
                                    if (user != null) {

                                        val post = Post(description, image_url, user.username, createdAt)

                                        postList.add(post)
                                        println(postList.size)
                                        println(querySnapshot.size())

                                    }
                                }

                                // Check if all documents have been processed
                                if (postList.size == querySnapshot.size()) {
                                    println(postList)
                                    val postAdapter = PostsAdapter(this, postList)
                                    recyclerView.adapter = postAdapter
                                }
                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error retrieving posts: ${exception.message}")
            }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_profile){
            val intent = Intent(this,ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, signedInUser?.username)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}