package com.example.brdplay.ui.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.brdplay.R
import com.example.brdplay.models.Group
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GroupsAdapter(private val activeUsername: String, private var dataSet: List<Group>): RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
    class ViewHolder(private val activeUsername: String, view: View) : RecyclerView.ViewHolder(view) {
        private val nameView: TextView = view.findViewById(R.id.group_name)
        private val expandButton: ImageButton = view.findViewById(R.id.expand_button)
        private val listView: TextView = view.findViewById(R.id.group_list)
        private val leaveGroup: ImageButton = view.findViewById(R.id.leave_group)

        fun bind(group: Group) {
            nameView.text = group.name
            listView.text = group.members.joinToString(separator = ", ")
            expandButton.setOnClickListener {
                if(listView.visibility == View.GONE) {
                    listView.visibility = View.VISIBLE
                    expandButton.setImageResource(R.drawable.ic_baseline_expand_less_24)
                } else {
                    listView.visibility = View.GONE
                    expandButton.setImageResource(R.drawable.ic_baseline_expand_more_24)
                }
            }
            leaveGroup.setOnClickListener {
                Firebase.firestore
                    .collection("groups")
                    .document(group.id)
                    .update("members", FieldValue.arrayRemove(activeUsername))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_cardview, parent, false)

        return ViewHolder(activeUsername, view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    fun setGroups(newDataSet: List<Group>) {
        this.dataSet = newDataSet
        notifyDataSetChanged()
    }
}