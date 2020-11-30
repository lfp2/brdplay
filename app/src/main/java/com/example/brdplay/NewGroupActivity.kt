package com.example.brdplay

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
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
        val memberEdit: AutoCompleteTextView = findViewById(R.id.member_edit_text)
        val membersChipsGroup: ChipGroup = findViewById(R.id.members_chips)

        // Add creator to members
        addChipToGroup(activeUsername, membersChipsGroup)

        print(sharedViewModel.users.toTypedArray())

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sharedViewModel.users.toTypedArray())
        memberEdit.setAdapter(adapter)

        memberEdit.onItemClickListener = AdapterView.OnItemClickListener{
                parent,_,position,_->
            val selectedItem = parent.getItemAtPosition(position).toString()
            checkAndAddChipToGroup(selectedItem.toString(), membersChipsGroup)
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