package com.cherenkevich.drivex.data.model.api_models

import androidx.room.PrimaryKey
import java.io.Serializable

data class ParkingExpense(
    @PrimaryKey(autoGenerate = true)
    val baseExpenseModel: BaseExpenseModel,
    var uid: Int = 0,
    var longtitudeParkingPlace: Long = 0L,
    var latitudeParkingPlace: Long = 0L,
    var timeParkingPeriod: String = "",
    var mapScreenPlaceURL: String = ""
) : Serializable