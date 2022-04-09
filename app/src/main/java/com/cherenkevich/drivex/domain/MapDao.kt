package com.cherenkevich.drivex.domain

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cherenkevich.drivex.data.model.MapModels

@Dao
interface MapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(mapmodels: MapModels)

    @Delete
    suspend fun deleteRun(mapmodels: MapModels)

    @Query("SELECT * FROM map_table ORDER BY timestamp DESC")
    fun getAllDriveSortedByDate(): LiveData<List<MapModels>>

    @Query("SELECT * FROM map_table ORDER BY timeInMillis DESC")
    fun getAllDriveSortedByTimeInMillis(): LiveData<List<MapModels>>

    @Query("SELECT * FROM map_table ORDER BY distanceInMeters DESC")
    fun getAllDriveSortedByDistance(): LiveData<List<MapModels>>

    @Query("SELECT SUM(timeInMillis) FROM map_table")
    fun getTotalTimeInMillis(): LiveData<Long>

    @Query("SELECT SUM(distanceInMeters) FROM map_table")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT AVG(avgSpeedInKMH) FROM map_table")
    fun getTotalAvgSpeed(): LiveData<Float>

}