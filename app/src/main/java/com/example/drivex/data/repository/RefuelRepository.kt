package com.example.drivex.data.repository

import androidx.lifecycle.LiveData
import com.example.drivex.data.model.Refuel

interface RefuelRepository {
    fun getRefuelById(id: Long): LiveData<Refuel>
    suspend fun insert(refuel: Refuel)
    suspend fun getAllRefuel(): List<Refuel>
    suspend fun addRefuel(refuel: Refuel)
    suspend fun getSumOfExpenses(): LiveData<Double>
    suspend fun getLastMileage(): LiveData<Int>
}