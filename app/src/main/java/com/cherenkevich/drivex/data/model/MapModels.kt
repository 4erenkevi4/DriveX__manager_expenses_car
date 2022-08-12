package com.cherenkevich.drivex.data.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "map_table")
data class MapModels(
    val uid: Int = 0,
    var img: Bitmap? = null,
    var timestamp: Long = 0L,
    var avgSpeedInKMH: Float = 0f,
    var distanceInMeters: Int = 0,
    var timeInMillis: Long = 0,
    var maxSpeed: Long = 0L,
    var extraBrakes: Int = 0,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}