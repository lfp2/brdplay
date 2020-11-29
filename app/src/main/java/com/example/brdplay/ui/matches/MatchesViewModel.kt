package com.example.brdplay.ui.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brdplay.models.Group
import com.example.brdplay.models.Match
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.DateTime
import java.util.*

class MatchesViewModel : ViewModel() {
    private val _matches = MutableLiveData<List<Match>>()
    private var registration: ListenerRegistration? = null

    fun matches(update: Boolean = false): LiveData<List<Match>> {
        val calendar = Calendar.getInstance()
        val timeBegin = calendar.time
        calendar.add(Calendar.HOUR_OF_DAY, 2)
        val timeEnd = calendar.time

        if (update || registration == null) {
            if (registration != null) {
                registration!!.remove()
            }
            registration = Firebase.firestore
                .collection("matches")
                .whereGreaterThan("dateTime", timeBegin)
                .whereLessThan("dateTime", timeEnd)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val matches =
                        value!!.documents.map { doc -> doc!!.toObject(Match::class.java)!! }
                    _matches.postValue(matches)
                }
        }
        return _matches
    }
}