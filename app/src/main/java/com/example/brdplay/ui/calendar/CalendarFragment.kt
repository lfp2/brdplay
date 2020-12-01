package com.example.brdplay.ui.calendar

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
import com.example.brdplay.NewMatchActivity
import com.example.brdplay.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CalendarFragment : Fragment() {

    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        calendarViewModel =
                ViewModelProvider(this).get(CalendarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_calendar, container, false)
        val activeUsername = requireActivity().intent.getStringExtra("activeUsername")!!

        val newGameButton: FloatingActionButton = root.findViewById(R.id.new_match)
        newGameButton.setOnClickListener {
            val intent = Intent(this.context, NewMatchActivity::class.java)
            intent.putExtra("activeUsername", activeUsername)
            this.context?.startActivity(intent)
        }

        val adapter = CalendarAdapter(emptyList())
        calendarViewModel.calendar(activeUsername).observe(viewLifecycleOwner, Observer { matches ->
            adapter.setMatches(matches)
        })
        val layoutManager = LinearLayoutManager(this.context)

        val recyclerView: RecyclerView =  root.findViewById(R.id.calendar_recycler_view)
        recyclerView.apply {
            this.layoutManager = layoutManager
            this.adapter = adapter
        }
        return root
    }
}