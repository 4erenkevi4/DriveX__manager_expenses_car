package com.example.drivex.data.repository

import androidx.lifecycle.LiveData
import com.example.drivex.data.model.Refuel
import com.example.drivex.data.RefuelDao
import javax.inject.Inject


class RefuelRepositoryImpl @Inject constructor(
    val refuelDao: RefuelDao
) : RefuelRepository {

    override suspend fun insert(refuel: Refuel) {
        return refuelDao.insert(refuel)
    }

    override fun getAllRefuel(): List<Refuel> = refuelDao.getAllRefuel()

    override fun getRefuelById(id: Long): LiveData<Refuel> {
        return refuelDao.getRefuelById(id)
    }

    override suspend fun addRefuel(refuel: Refuel) {
        refuelDao.addRefuel(refuel)
    }

    override fun getSumOfExpenses(): LiveData<Double> {
        return refuelDao.getSumOfExpenses()
    }

    override fun getLastMileage(): LiveData<Int> {
        return refuelDao.getLastMileage()
    }

    fun getSUmExpensesById(titleExp: String): LiveData<Int> {
        return refuelDao.getSUmExpensesById(titleExp)
    }

    fun getSummVolumeById(titleExp: String): LiveData<Int> {
        return refuelDao.getSummVolumeById(titleExp)
    }

}

