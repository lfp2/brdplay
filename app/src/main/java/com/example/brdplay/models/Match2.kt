package com.example.brdplay.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class Match2 (
    val location: MyLatLng? = null,
    val players: MutableList<String> = mutableListOf(),
    val url: String? = null,
    val game: String = "",
    val password: String? = null,
    val owner: String = "",
    val dateTime: Date = Date(),
    val id: String
) : Serializable {
    fun convertToMatch(): Match {
        val firestoreLocation = location?.let {
            GeoPoint(it.latitude, it.longitude)
        }
        return Match(firestoreLocation, players, url, game, password, owner, dateTime, id)
    }
}