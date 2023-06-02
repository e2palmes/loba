package com.example.loba.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.loba.R
import com.example.loba.models.User

class ContactsAdapter(context: Context, contacts: List<User>) :
    ArrayAdapter<User>(context, 0, contacts) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                R.layout.item_contact, parent, false
            )
        }

        val currentUser = getItem(position)

        val avatarImageView = listItemView?.findViewById<ImageView>(R.id.avatarImageView)
        val usernameTextView = listItemView?.findViewById<TextView>(R.id.usernameTextView)

        usernameTextView?.text = currentUser?.username

        // Load the profile picture if available, or use the default picture otherwise
        if (!currentUser?.profile_url.isNullOrEmpty()) {
            avatarImageView?.let {
                Glide.with(context)
                    .load(currentUser?.profile_url)
                    .placeholder(R.drawable.avatar_placeholder)
                    .into(it)
            }
        } else {
            avatarImageView?.setImageResource(R.drawable.avatar_placeholder)
        }

        return listItemView!!
    }
}