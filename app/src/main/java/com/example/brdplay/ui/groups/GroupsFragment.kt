package com.example.brdplay.ui.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brdplay.NewGroupActivity
import com.example.brdplay.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GroupsFragment : Fragment() {

    private lateinit var groupsViewModel: GroupsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        groupsViewModel =
                ViewModelProvider(this).get(GroupsViewModel::class.java)
        val activeUsername = requireActivity().intent.getStringExtra("activeUsername")!!

        val root = inflater.inflate(R.layout.fragment_groups, container, false)
        val newGroupButton : FloatingActionButton = root.findViewById(R.id.new_group)
        newGroupButton.setOnClickListener {
            val intent = Intent(this.context, NewGroupActivity::class.java)
            intent.putExtra("activeUsername", activeUsername)
            this.context?.startActivity(intent)
        }

        val adapter = GroupsAdapter(emptyList())
        groupsViewModel.groups(activeUsername).observe(viewLifecycleOwner, Observer { groups ->
            adapter.setGroups(groups)
        })

        val layoutManager = LinearLayoutManager(this.context)

        val recyclerView: RecyclerView =  root.findViewById(R.id.groups_recycler_view)
        recyclerView.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter
        }
        return root
    }
}