package com.example.brdplay

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.brdplay.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class SignInActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            updateUI(it, "batata")
        }
    }

    private fun updateUI(user: FirebaseUser, name: String) {
        writeNewUser(user.uid, name, user.email)

        // Go to MainActivity
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }

    private fun writeNewUser(userId: String, name: String, email: String?) {
        val user = User(name, email)
        database.child("users").child(userId).setValue(user)
    }
}