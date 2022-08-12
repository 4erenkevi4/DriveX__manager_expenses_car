package com.cherenkevich.drivex.data.model.api_models

import androidx.room.PrimaryKey
import java.io.Serializable

data class Washing(
    @PrimaryKey(autoGenerate = true
)

val baseExpenseModel: BaseExpenseModel,
var uid: Int = 0,
var longtitudeWashing: Long = 0L,
var latitudeWashinge: Long = 0L,
var mapScreenPlaceURL: String = ""
) : Serializable