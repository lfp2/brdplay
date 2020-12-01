package com.example.brdplay

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.brdplay.models.Group
import com.example.brdplay.ui.groups.GroupsViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewGroupActivity : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel
    private val groupMembers: MutableSet<String> = mutableSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel =
            ViewModelProvider(this).get(SharedViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_newgroup)
        val activeUsername = intent.getStringExtra("activeUsername")!!

        val createButton : Button = findViewById(R.id.buttonCreateGroup)
        val nameView : EditText = findViewById(R.id.editTextGroupName)
        val memberEdit: EditText = findViewById(R.id.member_edit_text)
        val membersChipsGroup: ChipGroup = findViewById(R.id.members_chips)

        // Add creator to members
        addChipToGroup(activeUsername, membersChipsGroup)

        memberEdit.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val memberUsername = v.text
                if (!memberUsername.isNullOrEmpty()) {
                    checkAndAddChipToGroup(memberUsername.toString(), membersChipsGroup)
                    v.text = ""
                }
                return@setOnEditorActionListener true
            }
            false
        }

        memberEdit.setOnKeyListener { v, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_COMMA) {
                val memberUsername = memberEdit.text
                if (!memberUsername.isNullOrEmpty()) {
                    checkAndAddChipToGroup(memberUsername.toString(), membersChipsGroup)
                    memberEdit.text.clear()
                }
                return@setOnKeyListener true
            }
            false
        }

        createButton.setOnClickListener {
            val name = nameView.text.toString()
            val members = groupMembers.toList()
            val group = Group(name, members)
            Firebase.firestore
                .collection("groups")
                .add(group)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        finish()
                    } else {
                        Toast.makeText(this@NewGroupActivity, "Error while creating group", Toast.LENGTH_SHORT).show()
                    }
                }

            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun checkAndAddChipToGroup(username: String, chipGroup: ChipGroup) {
        if (!sharedViewModel.isValidUsername(username)) {
            Toast.makeText(this, "Not valid username", Toast.LENGTH_SHORT).show()
        } else if (groupMembers.contains(username)) {
            Toast.makeText(this, "User already in group", Toast.LENGTH_SHORT).show()
        } else {
            addChipToGroup(username, chipGroup)
        }
    }

    private fun addChipToGroup(username: String, chipGroup: ChipGroup) {
        val chip = Chip(this)
        chip.text = username
        chip.isCloseIconVisible = true

        chip.isClickable = false
        chip.isCheckable = false
        chipGroup.addView(chip as View)
        groupMembers.add(username)
        chip.setOnCloseIconClickListener {
            chipGroup.removeView(it)
            groupMembers.remove(username)
        }
    }
}