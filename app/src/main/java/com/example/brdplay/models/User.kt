package com.example.brdplay.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    val username: String = "",
    val email: String = ""
)