package com.example.brdplay

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.brdplay.models.Match
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class OpenMatchActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_matchgame)
        val match = intent.getSerializableExtra("match") as Match
        val gameNameView: TextView = findViewById(R.id.game_name)
        val playingTextView : TextView = findViewById(R.id.playing)
        val formatter = SimpleDateFormat("dd/MM/yy HH:mm")

        gameNameView.text = match.game
        playingTextView.text = resources.getString(R.string.playing, formatter.format(match.dateTime))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}