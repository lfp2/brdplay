package com.example.brdplay

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SharedViewModel : ViewModel() {
    var users: Set<String> = emptySet()
    init {
        val registration = Firebase.firestore
            .collection("users")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                users = value!!.documents.map { doc -> doc.getString("username")!! }.toSet()
            }
    }

    fun isValidUsername(username: String): Boolean {
        return users.contains(username)
    }
}