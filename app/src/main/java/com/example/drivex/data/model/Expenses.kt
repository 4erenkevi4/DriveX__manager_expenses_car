package com.example.drivex.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "refuel")
data class Expenses(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String,
    var description: String,
    var mileage: Int = 0,
    var volume: Int = 0,
    var totalSum: Double = 0.0,
    var date: String = "",
    var icon: Int? = null,
    val photoURI: String? = null,
    val timeForMillis : Long? = null
) : Serializable