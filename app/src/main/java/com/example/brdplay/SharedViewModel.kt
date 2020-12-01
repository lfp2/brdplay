package com.example.brdplay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brdplay.models.Group
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SharedViewModel : ViewModel() {
    var users: Set<String> = emptySet()
    var groups: Map<String, List<String>> = emptyMap()

    init {
        Firebase.firestore
            .collection("users")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                users = value!!.documents.map { doc -> doc.getString("username")!! }.toSet()
            }

        Firebase.firestore
            .collection("groups")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val tempGroup = value!!.documents.map { doc -> doc!!.toObject(Group::class.java)!! }
                groups = tempGroup.map { it.name to it.members }.toMap()
            }
    }

    fun isValidUsername(username: String): Boolean {
        return users.contains(username)
    }

    fun getMembers(groupName: String): List<String>? {
        Log.d("shared", "got ${groups.size}, query: $groupName ${groups}")
        val answer = groups.get(groupName)
        Log.d("shared", "ans: $answer")
        return answer
    }
}