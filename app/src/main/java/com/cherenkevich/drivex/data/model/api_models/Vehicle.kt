package com.cherenkevich.drivex.data.model.api_models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "vehicle")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    var model: String = "",
    var brand: String = "",
    var typeFuel: String,
    var onCityConsumption: Double = 0.0,
    var offCityConsumption: Double = 0.0,
    var maxFuelVolume: Int = 0,
    var logoURL: String = "",
    var currentMileage: Int = 0,
) : Serializable
