package com.example.drivex.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.drivex.data.model.MapModels
import com.example.drivex.data.model.Refuel

@Dao
interface RefuelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRefuel(refuel: Refuel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(refuel: Refuel)

    @Delete
    suspend fun delete(refuel: Refuel)

    @Query("DELETE FROM refuel WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM refuel ORDER BY date ASC")
    fun getAllRefuel(): List<Refuel>

    @Query("SELECT * FROM refuel WHERE id = :id")
    fun getRefuelById(id: Long): LiveData<Refuel>

    @Query("SELECT SUM(totalSum) FROM refuel")
    fun getSumOfExpenses(): LiveData<Double>

    @Query("SELECT * FROM refuel ORDER BY totalSum DESC")
    fun getAllSortedByTotalSumm(): LiveData<List<Refuel>>

    @Query("SELECT mileage FROM refuel ORDER BY date DESC LIMIT 1")
    fun getLastMileage(): LiveData<Int>

    @Query("SELECT SUM(totalSum) FROM refuel WHERE title = :titleExp")
    fun getSUmExpensesById(titleExp:String): LiveData<Int>

    @Query("SELECT SUM(volume) FROM refuel WHERE title = :titleExp")
    fun getSummVolumeById(titleExp:String): LiveData<Int>

    @Query("SELECT mileage FROM refuel ORDER BY date DESC LIMIT 1")
    fun getLastMileageInt(): Int

    @Query("SELECT SUM(totalSum) FROM refuel WHERE title = :titleExp")
    fun getSUmExpensesIntById(titleExp:String): Int

}
