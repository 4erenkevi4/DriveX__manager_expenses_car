package com.example.drivex.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.drivex.data.model.Expenses
import com.example.drivex.domain.ExpensesDao
import com.example.drivex.data.ExpensesRoomDatabase


class ExpensesRepositoryImpl(application: Application) :
    ExpensesRepository {
    private val refuelDao: ExpensesDao by lazy {
        val db = ExpensesRoomDatabase.getInstance(application)
        db.refuelDao()
    }

    override suspend fun insert(expenses: Expenses) {
        return refuelDao.insert(expenses)
    }

    override suspend fun delete(expenses: Expenses) {
        refuelDao.delete(expenses)
    }

    override suspend fun deletebyId(id: Long) {
        refuelDao.deleteById(id)
    }

    override suspend fun getAllRefuel(): List<Expenses> = refuelDao.getAllRefuel()

    override fun getAllExpensesBydate() = refuelDao.getAllExpensesBydate()

    override fun getAllExpensesByPeriod(period: Long) = refuelDao.getAllExpensesFromPeriod(period)
    override fun getExpensesBetweenPeriods(
        minPeriod: Int,
        maxPeriod: Int
    ): LiveData<List<Expenses>> = refuelDao.getExpensesBetweenPeriods(minPeriod, maxPeriod)


    override fun getRefuelById(id: Long): LiveData<Expenses> {
        return refuelDao.getRefuelById(id)
    }

    override suspend fun addRefuel(expenses: Expenses) {
        refuelDao.addRefuel(expenses)
    }

    override suspend fun getSumOfExpenses(): LiveData<Double> {
        return refuelDao.getSumOfExpenses()
    }

    override suspend fun getLastMileage(): LiveData<Int> {
        return refuelDao.getLastMileage()
    }

    override suspend fun getSUmExpensesIntById(key: String): Int {
        return refuelDao.getSUmExpensesIntById(key)
    }


}
