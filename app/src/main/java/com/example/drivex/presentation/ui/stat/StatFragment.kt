package com.example.drivex.presentation.ui.stat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R

class StatFragment : Fragment() {

    private lateinit var statViewModel: StatViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        statViewModel =
                ViewModelProvider(this).get(StatViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_stat, container, false)
        val textView: TextView = root.findViewById(R.id.text_stat)
        statViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}