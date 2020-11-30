package com.example.brdplay.ui.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.example.brdplay.R
import com.example.brdplay.models.Group

class GroupsAdapter(private var dataSet: List<Group>): RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameView: TextView = view.findViewById(R.id.group_name)
        private val buttonView: ImageButton = view.findViewById(R.id.expand_button)
        private val listView: ListView = view.findViewById(R.id.group_list)

        fun bind(group: Group) {
            val adapter = ArrayAdapter<String>(listView.context, android.R.layout.simple_list_item_1, group.members)
            nameView.text = group.name
            listView.adapter = adapter
            buttonView.setOnClickListener {
                if(listView.visibility == View.GONE) {
                    listView.visibility = View.VISIBLE
                    buttonView.setImageResource(R.drawable.ic_baseline_expand_less_24)
                } else {
                    listView.visibility = View.GONE
                    buttonView.setImageResource(R.drawable.ic_baseline_expand_more_24)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_cardview, parent, false)

        return ViewHolder(view)
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