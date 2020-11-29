package com.example.brdplay.models

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class MyLatLng(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Serializable