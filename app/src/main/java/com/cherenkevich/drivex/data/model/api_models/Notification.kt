package com.cherenkevich.drivex.data.model.api_models

import androidx.room.PrimaryKey
import java.io.Serializable

data class Notification(
    @PrimaryKey(autoGenerate = true)
    var category: String,
    var description: String,
    var isDone: Boolean = false,
    val baseTimeModel: BaseTimeModel
) : Serializable