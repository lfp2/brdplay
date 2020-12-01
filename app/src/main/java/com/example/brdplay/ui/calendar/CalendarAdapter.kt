package com.example.brdplay.ui.calendar

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.brdplay.OpenMatchActivity
import com.example.brdplay.R
import com.example.brdplay.models.Group
import com.example.brdplay.models.Match
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class CalendarAdapter(private val activeUsername: String, private var dataSet: List<Match>): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    class ViewHolder(private val activeUsername: String, private val view: View) : RecyclerView.ViewHolder(view) {
        private val context = view.context
        private val gameName: TextView = view.findViewById(R.id.game_name)
        private val matchDate: TextView = view.findViewById(R.id.match_date)
        private val leaveGame: ImageButton = view.findViewById(R.id.leave_game)

        fun bind(match: Match) {
            gameName.text = match.game
            val formatter = SimpleDateFormat("dd/MM/yy HH:mm")
            matchDate.text = formatter.format(match.dateTime)
            view.setOnClickListener {
                val intent = Intent(context, OpenMatchActivity::class.java)
                intent.putExtra("match", match.convertToMatch2())
                context.startActivity(intent)
            }
            leaveGame.setOnClickListener {
                Firebase.firestore
                    .collection("matches")
                    .document(match.id)
                    .update("players", FieldValue.arrayRemove(activeUsername))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_cardview, parent, false)

        return ViewHolder(activeUsername, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    fun setMatches(newDataSet: List<Match>) {
        this.dataSet = newDataSet
        notifyDataSetChanged()
    }
}