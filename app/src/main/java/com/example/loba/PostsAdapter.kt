package com.example.loba

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loba.models.Post

class PostsAdapter (val context: Context, val posts: List<Post>) :
    RecyclerView.Adapter<PostsAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val post = posts[position]
        holder.tvUsername.text = post.user?.username.toString()
        holder.tvDescription.text = post.description.toString()
        Glide.with(context).load(post.image_url).into(holder.image_url)
        holder.tvCreated.text = post.created_at.toString()
    }

    inner class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image_url: ImageView = itemView.findViewById(R.id.ivPost)
        val tvUsername : TextView = itemView.findViewById(R.id.tvUsername)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvCreated: TextView = itemView.findViewById(R.id.tvCreatedAt)
    }

}