package com.example.brdplay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.brdplay.models.Match2
import java.text.SimpleDateFormat


class OpenMatchActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_matchgame)
        val match = intent.getSerializableExtra("match") as Match2
        val gameNameView: TextView = findViewById(R.id.game_name)
        val playingTextView : TextView = findViewById(R.id.playing)
        val actionButton : Button = findViewById(R.id.match_action_button)
        val userList : ListView = findViewById(R.id.match_players)

        if(match.url != null) {
            actionButton.text = resources.getString(R.string.join_call)
            actionButton.setOnClickListener {
                var url = match.url
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
        } else {
            actionButton.text = resources.getString(R.string.open_map)
            actionButton.setOnClickListener {
                val url = String.format("geo:0,0?q=%f,%f(game+location)", match.location!!.latitude, match.location.longitude)
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
            }
        }

        gameNameView.text = match.game
        val formatter = SimpleDateFormat("dd/MM/yy HH:mm")
        playingTextView.text = resources.getString(
            R.string.playing,
            formatter.format(match.dateTime)
        )
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, match.players)
        userList.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}