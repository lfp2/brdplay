package com.example.brdplay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        auth = Firebase.auth
        database = Firebase.database.reference
        buttonSignIn.setOnClickListener {
            signIn(
                signInEmailAddress.text.toString(),
                signInPassword.text.toString()
            )
        }
        buttonSignUp.setOnClickListener {
            goToRegister()
        }

    }

    public override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            goToMainActivity(it)
        }
    }

    private fun goToMainActivity(user: FirebaseUser?) {
        // Go to MainActivity
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun goToRegister() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    goToMainActivity(auth.currentUser!!)
                } else {
                    Toast.makeText(baseContext, "Invalid Email or Password",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}