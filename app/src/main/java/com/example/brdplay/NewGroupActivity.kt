package com.example.brdplay

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewGroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_newgroup)
        val createButton : Button = findViewById(R.id.buttonCreateGroup)
        val editText : EditText = findViewById(R.id.editTextGroupName)
        createButton.setOnClickListener {
            Toast.makeText(this, editText.text, Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}