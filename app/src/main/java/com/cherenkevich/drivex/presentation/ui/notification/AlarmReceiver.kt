package com.cherenkevich.drivex.presentation.ui.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cherenkevich.drivex.R
import com.cherenkevich.drivex.presentation.ui.activity.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val title: String? = intent.getStringExtra("Title")
        val description: String? = intent.getStringExtra("Description")
        val id: Int = intent.getIntExtra("id",1)
        val notificationUtils = NotificationUtils(context)
        val notification = notificationUtils.getNotificationBuilder(title, description).build()
        notificationUtils.getManager().notify(id, notification)
    }
}

class NotificationUtils(base: Context) : ContextWrapper(base) {
    val MYCHANNEL_ID = "App Alert Notification ID"
    val MYCHANNEL_NAME = "App Alert Notification"
    private var manager: NotificationManager? = null
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val channel =
            NotificationChannel(MYCHANNEL_ID, MYCHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(true)
        getManager().createNotificationChannel(channel)
    }

    fun getManager(): NotificationManager {
        if (manager == null) manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return manager as NotificationManager
    }

    fun getNotificationBuilder(title: String?, description: String?): NotificationCompat.Builder {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingFlags: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT;
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, pendingFlags)

        return NotificationCompat.Builder(applicationContext, MYCHANNEL_ID)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_notification_important)
            .setColor(Color.YELLOW)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
    }
}