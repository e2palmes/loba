package com.example.loba.adapters

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loba.R
import com.example.loba.models.Post
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PostsAdapter (val context: Context, val posts: List<Post>) :
    RecyclerView.Adapter<PostsAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val post = posts[position]
        holder.tvUsername.text = post.user.toString()
        holder.tvDescription.text = post.description.toString()
        Glide.with(context).load(post.image_url).into(holder.image_url)
        holder.tvCreated.text =  convertTimestampToDate(post.createdAt)
    }

    inner class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image_url: ImageView = itemView.findViewById(R.id.ivPost)
        val tvUsername : TextView = itemView.findViewById(R.id.tvUsername)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvCreated: TextView = itemView.findViewById(R.id.tvCreatedAt)
    }

    fun convertTimestampToDate(timestamp: Timestamp): String {
        val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        val date = Date(timestamp.seconds * 1000)
        return dateFormat.format(date)
    }

}