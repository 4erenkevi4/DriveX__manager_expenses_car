package com.example.drivex.presentation.ui.activity.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.example.drivex.data.RefuelDao
import com.example.drivex.data.RefuelRoomDatabase
import com.example.drivex.data.model.Refuel
import com.example.drivex.data.repository.RefuelRepository
import com.example.drivex.data.repository.RefuelRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

class AbstractViewModel(application: Application) : AndroidViewModel(application) {

private val refuelRepository:RefuelRepository
init {
    refuelRepository = RefuelRepositoryImpl(application)
    }
    private val refuelDao: RefuelDao by lazy {
        val db = RefuelRoomDatabase.getInstance(application)
        db.refuelDao()
    }


     suspend fun readAllDataByDate(): List<Refuel> {
        return refuelRepository.getAllRefuel()
    }

     fun addRefuel(refuel: Refuel) {
        viewModelScope.launch(Dispatchers.IO) {
            refuelRepository.addRefuel(refuel)
        }
    }

    val serviceCostSum: LiveData<String> = Transformations.map(refuelDao.getSumOfExpenses()) { sumOfCosts ->
        NumberFormat.getCurrencyInstance(Locale("ru", "BY")).format(sumOfCosts?: 0.0)
    }

    val lastMileage: LiveData<Int> = refuelDao.getLastMileage()
    val lastMileageStr: LiveData<String> = Transformations.map(lastMileage){ checkpoint ->
        (checkpoint?.toString() ?: "0") + " Km"
    }

}