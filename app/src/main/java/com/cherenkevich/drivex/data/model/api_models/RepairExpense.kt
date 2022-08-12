package com.cherenkevich.drivex.data.model.api_models

import androidx.room.PrimaryKey
import java.io.Serializable

data class RepairExpense(
    @PrimaryKey(autoGenerate = true)
    val baseExpenseModel: BaseExpenseModel,
    var uid: Int = 0,
    var longtitudeSTO: Long = 0L,
    var latitudeSTO: Long = 0L,
    var mileage: Int = 0,
    var repairTypes: List<String> = listOf(),
    var details: List<String> = listOf(),
    var costOfWorking: Double = 0.0,
    var costOfDetails: Double = 0.0,
) : Serializable