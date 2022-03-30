package com.example.drivex.data.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "map_table")
data class MapModels(
    var img: Bitmap? = null,
    var timestamp: Long = 0L,
    var avgSpeedInKMH: Float = 0f,
    var distanceInMeters: Int = 0,
    var timeInMillis: Long = 0,
    var maxSpeed:Long = 0L
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}