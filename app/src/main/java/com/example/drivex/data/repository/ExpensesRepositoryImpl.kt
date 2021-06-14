package com.example.drivex.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.drivex.data.model.Refuel
import com.example.drivex.domain.ExpensesDao
import com.example.drivex.data.ExpensesRoomDatabase


class ExpensesRepositoryImpl(application: Application) :
    ExpensesRepository {
    private val refuelDao: ExpensesDao by lazy {
        val db = ExpensesRoomDatabase.getInstance(application)
        db.refuelDao()
    }

    override suspend fun insert(refuel: Refuel) {
       return refuelDao.insert(refuel)
    }

    override suspend fun delete(refuel: Refuel) {
        refuelDao.delete(refuel)
    }

    override suspend fun deletebyId(id: Long) {
        refuelDao.deleteById(id)
    }

    override suspend fun getAllRefuel(): List<Refuel> =refuelDao.getAllRefuel()

    override fun getAllExpensesBydate() = refuelDao.getAllExpensesBydate()

    override fun getRefuelById(id: Long): LiveData<Refuel> {
        return refuelDao.getRefuelById(id)
    }
    override suspend fun addRefuel(refuel: Refuel) {
        refuelDao.addRefuel(refuel)
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
