package com.example.brdplay.ui.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.brdplay.R

class MatchesFragment : Fragment() {
    private lateinit var matchesViewModel: MatchesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        matchesViewModel =
                ViewModelProvider(this).get(MatchesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_matches, container, false)
        val activeUsername = requireActivity().intent.getStringExtra("activeUsername")!!

        val adapter = MatchesAdapter(activeUsername, emptyList())
        matchesViewModel.matches().observe(viewLifecycleOwner, Observer { matches ->
            adapter.setMatches(matches)
        })

        val layoutManager = LinearLayoutManager(this.context)

        val recyclerView: RecyclerView = root.findViewById(R.id.matches_recycler_view)
        recyclerView.apply {
            this.adapter = adapter
            this.layoutManager = layoutManager
        }
        return root
    }
}