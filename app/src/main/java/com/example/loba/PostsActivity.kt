package com.example.loba

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loba.databinding.ActivityPostsBinding
import com.example.loba.databinding.ItemPostBinding
import com.example.loba.models.Post
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "PostsActivity"
class PostsActivity : AppCompatActivity() {

    val firestoreDb = Firebase.firestore
    private lateinit var posts: MutableList<Post>
    private lateinit var adapter: PostsAdapter
    private lateinit var binding: ActivityPostsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPosts.layoutManager = LinearLayoutManager(this)

//        Create data source
        posts = mutableListOf()

//        Get the data from FirestoreDB
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
            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()
            for (post in postList) {
                Log.i(TAG, "Post => $post")
            }
        }
        //        create the adapter
        adapter = PostsAdapter(this, posts)
        //        Binding
        binding.rvPosts.adapter = adapter
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