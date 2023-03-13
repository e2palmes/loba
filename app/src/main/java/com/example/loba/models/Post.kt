package com.example.loba.models

data class Post(
    var description: String = "",
    var image_url: String = "",
    var created_at: Long = 0,
    var user: User? = null
)