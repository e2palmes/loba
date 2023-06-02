package com.example.loba.models

import com.google.firebase.Timestamp

data class Post(
    var description: String,
    var image_url: String?,
    var user: String?,
    val createdAt: Timestamp
)