package com.cherenkevich.drivex.data.model.api_models

import androidx.room.PrimaryKey
import java.io.Serializable

data class BaseExpenseModel(
    @PrimaryKey(autoGenerate = true)
    var category: String,
    var description: String,
    var totalSum: Double = 0.0,
    val photoURL: String? = null,
    val baseTimeModel: BaseTimeModel
) : Serializable

data class BaseTimeModel(
    val timeForMillis : Long? = null,
    var date: String = "",
    val month: Int = 0,
    val year: Int = 0,
)