package com.example.drivex.data.repository

import androidx.lifecycle.LiveData
import com.example.drivex.data.model.MapModels
import com.example.drivex.data.model.Refuel

interface MapRepository {

    suspend fun insertMap(mapModels: MapModels): Unit
    suspend fun deleteRun(mapModels: MapModels): Unit
     fun getAllDriveSortedByDate(): LiveData<List<MapModels>>
    fun getAllDriveSortedByTimeInMillis(): LiveData<List<MapModels>>
    fun getAllDriveSortedByDistance(): LiveData<List<MapModels>>
    fun getAllDriveSortedByAvgSpeed(): LiveData<List<MapModels>>
    fun getTotalDistance(): LiveData<Int>
    fun getTotalTimeInMillis(): LiveData<Long>
    fun getTotalAvgSpeed(): LiveData<Float>

}