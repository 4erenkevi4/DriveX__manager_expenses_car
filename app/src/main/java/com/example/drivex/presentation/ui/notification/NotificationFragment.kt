package com.example.drivex.presentation.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R

class NotificationFragment : Fragment() {

    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationViewModel =
                ViewModelProvider(this).get(NotificationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notification, container, false)
        val textView: TextView = root.findViewById(R.id.text_notification)
        notificationViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}