package com.example.drivex.presentation.ui.notification

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.drivex.R
import com.example.drivex.utils.Constans.CHANNELID
import com.example.drivex.utils.Constans.DESCRIPTION

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private var info: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        createNotification()
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
   private fun createNotification() {
        val builder = NotificationCompat.Builder(applicationContext, CHANNELID)
            .setSmallIcon(R.drawable.ic_notification_important)
            .setContentText(initNotify())
            .setContentTitle(DESCRIPTION)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1234, builder.build())
        }
    }

    private fun initNotify(): String {

        info = "Продлите страховку "
        return info
    }
}
