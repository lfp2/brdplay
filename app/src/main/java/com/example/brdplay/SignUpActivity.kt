package com.example.brdplay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.brdplay.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = Firebase.auth
        database = Firebase.database.reference
        buttonSignIn.setOnClickListener {
            createAccount(
                    editTextPersonName.text.toString(),
                    editTextEmailAddress.text.toString(),
                    editTextPassword.text.toString()
            )
        }
    }

    public override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            updateUI(it)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Go to MainActivity
        if (user != null) {
            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun createAccount(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Created user.",
                        Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    writeNewUser(user!!.uid, name, email)
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_LONG).show()
                    Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun writeNewUser(userId: String, name: String, email: String?) {
        val user = User(name, email)
        database.child("users").child(userId).setValue(user)
    }
}