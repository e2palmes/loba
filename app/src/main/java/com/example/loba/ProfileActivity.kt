package com.example.loba

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "ProfileActivity"
class ProfileActivity : PostsActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_logout){
            Log.i(TAG,"User logging out")
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        if (item.itemId == R.id.menu_edit_profile){
            Log.i(TAG,"Edit profile")
            startActivity(Intent(this, EditProfileActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}