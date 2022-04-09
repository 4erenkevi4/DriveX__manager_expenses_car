package com.cherenkevich.drivex.data.repository

import com.cherenkevich.drivex.data.model.MapModels
import com.cherenkevich.drivex.domain.MapDao
import javax.inject.Inject


class MapRepository @Inject constructor(
    private val mapDao: MapDao
) {

    suspend fun insertMap(mapModels: MapModels) = mapDao.insertRun(mapModels)

    suspend fun deleteRun(mapModels: MapModels) = mapDao.deleteRun(mapModels)

    fun getAllDriveSortedByDate() = mapDao.getAllDriveSortedByDate()

    fun getAllDriveSortedByTimeInMillis() = mapDao.getAllDriveSortedByTimeInMillis()

    fun getAllDriveSortedByDistance() = mapDao.getAllDriveSortedByDistance()

    fun getTotalAvgSpeed() = mapDao.getTotalAvgSpeed()

    fun getTotalDistance() = mapDao.getTotalDistance()

    fun getTotalTimeInMillis() = mapDao.getTotalTimeInMillis()


}