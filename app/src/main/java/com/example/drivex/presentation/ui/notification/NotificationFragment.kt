package com.example.drivex.presentation.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.drivex.R
import com.example.drivex.utils.Constans.CHANNELID
import com.example.drivex.utils.Constans.DESCRIPTION
import com.example.drivex.utils.Constans.NOTIFICATION
import java.util.concurrent.TimeUnit

class NotificationFragment : Fragment() {
    lateinit var addButton: TextView
    lateinit var notificationChannel: NotificationChannel
    lateinit var editText: EditText
    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notification, container, false)
        editText = root.findViewById(R.id.editText)
        addButton = root.findViewById(R.id.addButton)
        initNotificationChannel()
        addButton.setOnClickListener {
            CreateNotification()
        }
        return root
    }

    private fun CreateNotification() {
        Toast.makeText(context, NOTIFICATION, 300.toInt()).show()

        val notifyWork = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()
        val requestList = listOf(notifyWork)
        context?.let { WorkManager.getInstance(it).enqueue(requestList) }


    }
    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                CHANNELID,
                DESCRIPTION,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager: NotificationManager =
                requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}