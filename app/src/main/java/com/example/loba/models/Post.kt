package com.example.loba.models

import com.google.firebase.firestore.PropertyName

data class Post(
    var description: String = "",
    @get:PropertyName("image_url") @set:PropertyName("image_url") var image_url: String = "",
    @get:PropertyName("created_at") @set:PropertyName("created_at") var created_at: Long = 0,
    var user: User? = null
)