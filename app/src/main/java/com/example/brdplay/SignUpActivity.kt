package com.example.brdplay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brdplay.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = Firebase.auth
        database = Firebase.firestore
        buttonSignIn.setOnClickListener {
            createAccount(
                    editTextUsername.text.toString(),
                    editTextEmailAddress.text.toString(),
                    editTextPassword.text.toString()
            )
        }
    }

    public override fun onStart() {
        super.onStart()
        auth.currentUser?.let {user ->
            database.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener { result ->
                    goToMainActivity(result.getString("username")!!)
                }
        }
    }

    private fun goToMainActivity(username: String) {
        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
        intent.putExtra("activeUsername", username)
        startActivity(intent)
    }

    private fun createAccount(username: String, email: String, password: String) {
        checkUsername(username).addOnCompleteListener {
            if (it.result!!) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@SignUpActivity) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            writeNewUser(user!!.uid, username, email)
                            goToMainActivity(username)
                        } else {
                            errorMessage.text = task.exception.toString()
                        }
                    }
            } else {
                errorMessage.text = "username already exists"
            }
        }

    }

    private fun checkUsername(username: String): Task<Boolean> {
        return database
            .collection("users")
            .whereEqualTo("username", username)
            .get()
            .continueWith { task -> task.result!!.isEmpty }
    }


    private fun writeNewUser(userId: String, name: String, email: String) {
        val user = User(name, email)
        database.collection("users").document(userId).set(user)
    }
}