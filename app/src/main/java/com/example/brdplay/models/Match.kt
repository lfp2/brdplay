package com.example.brdplay.models

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@IgnoreExtraProperties
data class Match (
    val location: String? = null,
    val players: List<String> = emptyList(),
    val url: String? = null,
    val game: String = "",
    val password: String? = null,
    val owner: String = "",
    val dateTime: Date = Date()
) : Serializable