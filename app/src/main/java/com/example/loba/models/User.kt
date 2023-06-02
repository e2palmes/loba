package com.example.loba.models

data class User(
    val id: String? ="",
    var fullname: String ="",
    var username: String = "",
    var email : String = "",
    var profile_url : String? = ""
)