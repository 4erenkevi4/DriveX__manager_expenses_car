package com.example.drivex.presentation.ui.activity.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.drivex.data.RefuelDao
import com.example.drivex.data.RefuelRoomDatabase
import com.example.drivex.data.model.MapModels
import com.example.drivex.data.model.Refuel
import com.example.drivex.data.repository.RefuelRepository
import com.example.drivex.data.repository.RefuelRepositoryImpl
import com.example.drivex.utils.SortType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.NumberFormat
import java.util.*

class AbstractViewModel(application: Application) : AndroidViewModel(application) {

    private val refuelRepository: RefuelRepository


    init {
        refuelRepository = RefuelRepositoryImpl(application)
    }
    private val runsSortedByDate = refuelRepository.getAllSortedByTotalSumm()

    private val refuelDao: RefuelDao by lazy {
        val db = RefuelRoomDatabase.getInstance(application)
        db.refuelDao()
    }
    val expens = MediatorLiveData<List<Refuel>>()
    var sortType = SortType.DATE

    /**
     * Posts the correct run list in the LiveData
     */
    init {
        expens.addSource(runsSortedByDate) { result ->
            Timber.d("RUNS SORTED BY DATE")
            if (sortType == SortType.DATE) {
                result?.let { expens.value = it }
            }
        }
    }
    
    suspend fun readAllDataByDate(): List<Refuel> {
        return refuelRepository.getAllRefuel()
    }

    suspend fun totalRef(key: String): Int {
        return refuelRepository.getSUmExpensesIntById(key)
    }

    fun readRefuelById(id: Long): LiveData<Refuel> {
        return refuelRepository.getRefuelById(id)
    }

    fun addRefuel(refuel: Refuel) {
        viewModelScope.launch(Dispatchers.IO) {
            refuelRepository.addRefuel(refuel)
        }
    }

    fun insert(refuel: Refuel) {
        viewModelScope.launch(Dispatchers.IO) {
            refuelRepository.insert(refuel)
        }
    }

    fun delete(refuel: Refuel) = viewModelScope.launch {
        refuelRepository.delete(refuel)
    }

    val allExpensesSum: LiveData<String> =
        Transformations.map(refuelDao.getSumOfExpenses()) { sumOfCosts ->
            NumberFormat.getCurrencyInstance(Locale("ru", "BY")).format(sumOfCosts ?: 0.0)
        }
    val allFuelCostSum: LiveData<String> =
        Transformations.map(refuelDao.getSUmExpensesById("Заправка")) { sumOfCosts ->
            NumberFormat.getCurrencyInstance(Locale("ru", "BY")).format(sumOfCosts ?: 0.0)
        }

    val allServiceCostSum: LiveData<String> =
        Transformations.map(refuelDao.getSUmExpensesById("Сервис")) { sumOfCosts ->
            NumberFormat.getCurrencyInstance(Locale("ru", "BY")).format(sumOfCosts ?: 0.0)
        }

    val refuelSum: LiveData<Int> = refuelDao.getSUmExpensesById("Заправка")
    val serviceSum: LiveData<Int> = refuelDao.getSUmExpensesById("Сервис")
    val shoppingSum: LiveData<Int> = refuelDao.getSUmExpensesById("Покупка")
    val paymentsSum: LiveData<Int> = refuelDao.getSUmExpensesById("Платеж")

     val allFuelVolumeSumInt: LiveData<Int> = refuelDao.getSummVolumeById("Заправка")
    val allFuelVolumeSum: LiveData<String> = Transformations.map(allFuelVolumeSumInt) { summ ->
        (summ?.toString() ?: "0") + " Л."
    }

    val lastMileage: LiveData<Int> = refuelDao.getLastMileage()
    val lastMileageStr: LiveData<String> = Transformations.map(lastMileage) { checkpoint ->
        (checkpoint?.toString() ?: "0") + " Km."
    }


}