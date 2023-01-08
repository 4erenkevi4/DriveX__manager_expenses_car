package com.cherenkevich.drivex.data.model.api_models

import androidx.room.PrimaryKey
import java.io.Serializable

data class RefuelExpense(
    @PrimaryKey(autoGenerate = true)
    val baseExpenseModel: BaseExpenseModel,
    var uid: Int = 0,
    var mileage: Int = 0,
    var volume: Int = 0,
    var fuelType: String = "",
    var costOfLitre: Double = 0.0,
    var isFuelTank: Boolean = false
) : Serializable