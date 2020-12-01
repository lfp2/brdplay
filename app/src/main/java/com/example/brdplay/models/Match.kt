package com.example.brdplay.models

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint
import java.util.*

@IgnoreExtraProperties
data class Match (
    val location: GeoPoint? = null,
    val players: MutableList<String> = mutableListOf(),
    val url: String? = null,
    val game: String = "",
    val password: String? = null,
    val owner: String = "",
    val dateTime: Date = Date(),
    @Exclude var id: String = ""
) {
    fun convertToMatch2(): Match2 {
        val myLocation = location?.let {
            MyLatLng(it.latitude, it.longitude)
        }
        return Match2(myLocation, players, url, game, password, owner, dateTime, id)
    }
}