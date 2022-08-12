package com.cherenkevich.drivex.data.model.api_models

import androidx.room.PrimaryKey
import java.io.Serializable

data class StatisticExpenses(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    var TotalSumExpenses: Double = 0.0,
    var refuelExpenses: Double = 0.0,
    var parkingExpenses: Double = 0.0,
    var washingExpenses: Int = 0,
    var RepairExpenses: Double = 0.0,
    var FinanseExpenses: Double = 0.0,
    var OtherExpenses: Int = 0,
) : Serializable