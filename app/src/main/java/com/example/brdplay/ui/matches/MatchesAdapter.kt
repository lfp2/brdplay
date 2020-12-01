package com.example.brdplay.ui.matches

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.brdplay.OpenMatchActivity
import com.example.brdplay.R
import com.example.brdplay.models.Match
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MatchesAdapter(private val activeUsername: String, private var dataSet: List<Match>): RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {
    class ViewHolder(private val activeUsername: String, private val view: View) : RecyclerView.ViewHolder(view) {
        private val context = view.context
        private val gameName: TextView = view.findViewById(R.id.match_game_name)
        private val owner: TextView = view.findViewById(R.id.match_owner_username)
        private val lock: ImageView = view.findViewById(R.id.match_private)

        fun bind(match: Match) {
            gameName.text = match.game
            owner.text = context.resources.getString(R.string.owner_phrase, match.owner)
            if (match.password == null) {
                lock.visibility = View.GONE
            } else {
                lock.visibility = View.VISIBLE
            }
            view.setOnClickListener {
                if (match.password != null) {
                    val builder = AlertDialog.Builder(context)
                    val passwordInput = EditText(context)
                    passwordInput.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    passwordInput.hint = context.getString(R.string.password)
                    passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()

                    builder.setTitle(R.string.password)
                    builder.setView(passwordInput)
                    builder.setPositiveButton(
                        R.string.join,
                        DialogInterface.OnClickListener { _, _ ->
                            if (passwordInput.text.toString() == match.password) {
                                joinMatch(match)
                            } else {
                                Toast.makeText(context, "Wrong password", Toast.LENGTH_LONG).show()
                            }
                        })
                    builder.setNegativeButton(
                        R.string.cancel,
                        DialogInterface.OnClickListener { dialog, _ ->
                            dialog.cancel()
                        })
                    builder.show()
                } else {
                    val builder = AlertDialog.Builder(context)

                    builder.setTitle(R.string.join_match)
                    builder.setPositiveButton(
                        R.string.join,
                        DialogInterface.OnClickListener { _, _ ->
                            joinMatch(match)
                        })
                    builder.setNegativeButton(
                        R.string.cancel,
                        DialogInterface.OnClickListener { dialog, _ ->
                            dialog.cancel()
                        })
                    builder.show()
                }
            }
        }

        fun joinMatch(match: Match) {
            if (!match.players.contains(activeUsername)) {
                match.players.add(activeUsername)

                // Add user to match player list in firebase
                Firebase.firestore
                    .collection("matches")
                    .document(match.id)
                    .update("players", FieldValue.arrayUnion(activeUsername))
            }
            val intent = Intent(context, OpenMatchActivity::class.java)
            intent.putExtra("match", match.convertToMatch2())
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.match_cardview,
            parent,
            false
        )

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