package com.example.drivex.data.repository
import android.app.Application
import com.example.drivex.data.MapRoomDatabase
import com.example.drivex.data.model.MapModels
import com.example.drivex.domain.MapDao


class MapRepositoryImpl(application: Application) :
    MapRepository {
    private val mapDao: MapDao by lazy {
        val db = MapRoomDatabase.getInstance(application)
        db.getmapDao()
    }

    override   suspend fun insertMap(mapModels: MapModels) = mapDao.insertRun(mapModels)

    override  suspend fun deleteRun(mapModels: MapModels) = mapDao.deleteRun(mapModels)

    override   fun getAllDriveSortedByDate() = mapDao.getAllDriveSortedByDate()

    override   fun getAllDriveSortedByTimeInMillis() = mapDao.getAllDriveSortedByTimeInMillis()

    override   fun getAllDriveSortedByDistance() = mapDao.getAllDriveSortedByDistance()

    override  fun getAllDriveSortedByAvgSpeed() = mapDao.getAllDriveSortedByAvgSpeed()

    override   fun getTotalDistance() = mapDao.getTotalDistance()

    override   fun getTotalTimeInMillis() = mapDao.getTotalTimeInMillis()

    override fun getTotalAvgSpeed() = mapDao.getTotalAvgSpeed()

}