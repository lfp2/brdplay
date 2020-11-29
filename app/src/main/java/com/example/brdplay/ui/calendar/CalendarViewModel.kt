package com.example.brdplay.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brdplay.models.Match
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class CalendarViewModel : ViewModel() {

    private val _calendar = MutableLiveData<List<Match>>()
    private var matchesRegistration: ListenerRegistration? = null
    private var registrationUser: String? = null

    fun calendar(username: String): LiveData<List<Match>> {
        if (registrationUser == null || registrationUser != username) {
            if (matchesRegistration != null) {
                matchesRegistration!!.remove()
            }
            matchesRegistration = Firebase.firestore
                .collection("matches")
                .whereArrayContains("players", username)
                .whereGreaterThan("dateTime", Date())
                .orderBy("dateTime")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val match = value!!.documents.map { doc -> doc!!.toObject(Match::class.java)!! }
                    _calendar.postValue(match)
                }
        }
        return _calendar
    }
}