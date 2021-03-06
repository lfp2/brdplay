package com.example.brdplay

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        auth = Firebase.auth
        database = Firebase.firestore
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
        auth.currentUser?.let {user ->
            goToMainActivity(user)
        }
    }

    private fun goToMainActivity(user: FirebaseUser) {
        database.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { result ->
                val intent = Intent(this, MainActivity::class.java)
                val username = result.getString("username")!!
                intent.putExtra("activeUsername", username)
                startActivity(intent)
            }
    }
    private fun goToRegister() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            errorMessage.text = "Invalid Email or Password"
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    goToMainActivity(auth.currentUser!!)
                } else {
                    errorMessage.text = "Invalid Email or Password"
                }
            }
    }
}