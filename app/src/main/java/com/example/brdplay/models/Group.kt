package com.example.brdplay.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Group (
    val name: String = "",
    val members: List<String> = emptyList(),
    @Exclude var id: String = ""
)