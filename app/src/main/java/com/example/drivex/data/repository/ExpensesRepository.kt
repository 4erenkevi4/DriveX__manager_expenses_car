package com.example.drivex.data.repository

import androidx.lifecycle.LiveData
import com.example.drivex.data.model.Expenses

interface ExpensesRepository {
    fun getRefuelById(id: Long): LiveData<Expenses>
    suspend fun insert(expenses: Expenses)
    suspend fun delete (expenses: Expenses)
    suspend fun deletebyId (id: Long)
    suspend fun getAllRefuel(): List<Expenses>
    suspend fun addRefuel(expenses: Expenses)
    suspend fun getSumOfExpenses(): LiveData<Double>
    suspend fun getLastMileage(): LiveData<Int>
    suspend fun getSUmExpensesIntById(key:String):Int
    fun getAllExpensesBydate(): LiveData<List<Expenses>>
    fun getAllExpensesByPeriod(period: Long): LiveData<List<Expenses>>
}