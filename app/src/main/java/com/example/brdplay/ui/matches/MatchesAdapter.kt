package com.example.brdplay.ui.matches

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import com.example.brdplay.NewGroupActivity
import com.example.brdplay.OpenMatchActivity
import com.example.brdplay.R
import com.example.brdplay.models.Match

class MatchesAdapter(private var dataSet: List<Match>): RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {
    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
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

                    builder.setTitle("Password")
                    builder.setView(passwordInput)
                    builder.setPositiveButton("JOIN", DialogInterface.OnClickListener { _, _ ->
                        if (passwordInput.text.toString() == match.password) {
                            val intent = Intent(context, OpenMatchActivity::class.java)
                            intent.putExtra("match", match)
                            context.startActivity(intent)
//                            Toast.makeText(context, "Right password", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Wrong password", Toast.LENGTH_LONG).show()
                        }
                    })
                    builder.setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.cancel()
                    })
                    builder.show()
                } else {
                    val builder = AlertDialog.Builder(context)

                    builder.setTitle("Join match")
                    builder.setPositiveButton("JOIN", DialogInterface.OnClickListener { _, _ ->
                        val intent = Intent(context, OpenMatchActivity::class.java)
                        intent.putExtra("match", match)
                        context.startActivity(intent)
//                        Toast.makeText(context, "Open match", Toast.LENGTH_LONG).show()
                    })
                    builder.setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.cancel()
                    })
                    builder.show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.match_cardview, parent, false)

        return ViewHolder(view)
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