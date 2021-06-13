package com.example.drivex.utils

import android.graphics.Color
import java.util.*

object Constans {
    const val CAMERA_PERMISSION_CODE = 1
    const val CAMERA_PIC_REQUEST = 2
    const val ACTIVITY_FUEL: Long = 11
    const val ACTIVITY_SERVICE: Long = 12
    const val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    // Service Intent Actions
    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"
    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    // Tracking Options
    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_UPDATE_INTERVAL = 2000L
    // Database
    const val DATABASE_NAME = "map_db"
    // Shared Preferences
    const val SHARED_PREFERENCES_NAME = "sharedPref"
    const val KEY_FIRST_TIME_TOGGLE = "KEY_FIRST_TIME_TOGGLE"
    // Map Options
    const val POLYLINE_COLOR = Color.GREEN
    const val POLYLINE_WIDTH = 11f
    const val MAP_ZOOM = 15f
    const val TIMER_UPDATE_INTERVAL = 50L
    // Notifications
    const val NOTIFICATION = "Напоминание создано!"
    const val DESCRIPTION = "Напоминание о вашем автомобиле:"
    const val CHANNELID = "notification_worker"
    const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1
    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val REFUEL = "Заправка"
    const val SERVICE = "Сервис"
    const val SHOPPING = "Покупка"
    const val PAYMENT = "Платеж"


    const val NOTIFY1 = "У Вас запланировано ТО"
    const val NOTIFY2 = "У Вас запланирована поездка"
    const val NOTIFY3 = "У Вас запланирована мойка авто"
    const val NOTIFY4 = "У Вас запланирован шиномонтаж"
    const val NOTIFY5 = "У Вас запланирована диагностика"
    const val NOTIFY6 = "Продлите страховку автомобиля!"
    const val NOTIFY7 = "Продлите водительские права!"
    const val NOTIFY8 = "Замените масло в вашем автомобиле!"
    const val NOTIFY9 = "Вас запланирован платеж"
    const val NOTIFY10 = "Пройдите техосмотр!"
    const val NOTIFY11 = "Вас запланирована покупка"
    const val NOTIFY12 = "Уведомление:"

}