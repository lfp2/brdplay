package com.example.brdplay.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.brdplay.R
import com.example.brdplay.SignInActivity
import com.example.brdplay.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        database = Firebase.firestore
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
//        val textView: TextView = root.findViewById(R.id.text_profile)
//        profileViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val signOutButton : Button = root.findViewById(R.id.buttonSignOut)
        signOutButton.setOnClickListener {
            signOut()
        }

        val activeUsername = requireActivity().intent.getStringExtra("activeUsername")!!
        val activeEmail = Firebase.auth.currentUser?.email!!
        val editTextUsername : EditText = root.findViewById(R.id.editTextUsername)
        editTextUsername.setText(activeUsername)
        val editTextEmail : EditText = root.findViewById(R.id.editTextEmailAddress)
        editTextEmail.setText(activeEmail)

        val editProfileButton : Button = root.findViewById(R.id.buttonEditProfile)
        editProfileButton.setOnClickListener {
            val changeRequest = userProfileChangeRequest {
                displayName = editTextUsername.text.toString()
            }
            editProfile(changeRequest)
        }

        return root
    }

    private fun signOut() {
        auth.signOut()
        requireActivity().run{
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun editProfile(changeRequest: UserProfileChangeRequest) {
        val user = auth.currentUser
        user!!.updateProfile(changeRequest).addOnCompleteListener {
            task ->
            if(task.isSuccessful) {
                database.collection("users")
                    .document(user!!.uid)
                    .update(mapOf( "username" to user!!.displayName))

                requireActivity().run {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                }
            } else {
                requireActivity().run {
                    Toast.makeText(this, "Error: Profile not updated", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}