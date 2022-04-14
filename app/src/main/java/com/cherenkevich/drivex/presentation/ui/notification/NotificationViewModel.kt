package com.cherenkevich.drivex.presentation.ui.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class NotificationViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notification Fragment"
    }
    val text: LiveData<String> = _text

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun startAlarm(
        context: Context,
        calendar: Calendar,
        notifyId: Int,
        titleOfNotification: String,
        editTextDesc: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        intent.putExtra("Title", titleOfNotification)
        intent.putExtra("Description", editTextDesc)
        intent.putExtra("id", notifyId)

        val pendingFlags: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT;
        }
        val pendingIntent = PendingIntent.getBroadcast(context, notifyId, intent, pendingFlags)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
            else -> {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }
    }

    fun makeActiveButtons(listButton: ArrayList<ImageView>) {
        listButton.forEach { it.isClickable = true }
        listButton.forEach { it.isFocusable = true }
        listButton.forEach { it.clearAnimation() }
    }


}