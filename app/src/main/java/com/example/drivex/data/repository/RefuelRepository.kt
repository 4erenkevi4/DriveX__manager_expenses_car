package com.example.drivex.data.repository

import androidx.lifecycle.LiveData
import com.example.drivex.data.model.Refuel

interface RefuelRepository {
    suspend fun getAllRefuel(): List<Refuel>
    suspend fun getRefuelById(id: Long): LiveData<Refuel>
    suspend fun addRefuel(refuel: Refuel)
    suspend fun getSumOfExpenses(): LiveData<Double>
    suspend fun getLastMileage(): LiveData<Int>
}