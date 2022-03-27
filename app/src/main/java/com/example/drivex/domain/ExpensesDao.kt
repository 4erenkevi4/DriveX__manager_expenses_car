package com.example.drivex.domain

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.drivex.data.model.Expenses

@Dao
interface ExpensesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRefuel(expenses: Expenses)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(expenses: Expenses)

    @Delete
    suspend fun delete(expenses: Expenses)

    @Query("DELETE FROM refuel WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM refuel ORDER BY date ASC")
    fun getAllRefuel(): List<Expenses>

    @Query("SELECT * FROM refuel ORDER BY id ASC")
    fun getAllExpensesBydate(): LiveData<List<Expenses>>

    @Query("SELECT * FROM refuel WHERE timeForMillis < :period")
    fun getAllExpensesFromPeriod(period:Long): LiveData<List<Expenses>>

    @Query("SELECT * FROM refuel WHERE id = :id")
    fun getRefuelById(id: Long): LiveData<Expenses>

    @Query("SELECT SUM(totalSum) FROM refuel")
    fun getSumOfExpenses(): LiveData<Double>

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
