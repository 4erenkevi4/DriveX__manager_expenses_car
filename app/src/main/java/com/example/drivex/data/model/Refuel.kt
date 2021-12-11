package com.example.drivex.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "refuel")
data class Refuel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String,
    var description: String,
    var mileage: Int = 0,
    var volume: Int = 0,
    var totalSum: Double = 0.0,
    var date: String = "",
    var icon: Int = 0,
    val photoURI: String? = null
) : Serializable