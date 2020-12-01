package com.example.brdplay.ui.groups

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brdplay.models.Group
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GroupsViewModel : ViewModel() {

    private val _groups = MutableLiveData<List<Group>>()
    private var groupsRegistration: ListenerRegistration? = null
    private var registrationUser: String? = null

    fun groups(username: String): LiveData<List<Group>> {
        if (registrationUser == null || registrationUser != username) {
            if (groupsRegistration != null) {
                groupsRegistration!!.remove()
            }
            groupsRegistration = Firebase.firestore
                .collection("groups")
                .whereArrayContains("members", username)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val groups = value!!.documents.map { doc ->
                        var group = doc!!.toObject(Group::class.java)!!
                        group.id = doc.id
                        group
                    }
                    _groups.postValue(groups)
                }
        }
        return _groups
    }
}