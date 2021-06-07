package com.example.drivex.data.repository

import androidx.lifecycle.LiveData
import com.example.drivex.data.model.Refuel

interface RefuelRepository {
    fun getRefuelById(id: Long): LiveData<Refuel>
    fun getSumOfExpenses(): LiveData<Double>
    fun getLastMileage(): LiveData<Int>
    fun getAllRefuel(): List<Refuel>
    suspend fun insert(refuel: Refuel)
    suspend fun addRefuel(refuel: Refuel)

}