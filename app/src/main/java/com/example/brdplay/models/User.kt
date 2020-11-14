package com.example.brdplay.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    var username: String? = "",
    var email: String? = "",
    var location: String? = ""
)