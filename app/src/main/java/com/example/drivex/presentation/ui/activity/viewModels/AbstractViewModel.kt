package com.example.drivex.presentation.ui.activity.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.drivex.domain.ExpensesDao
import com.example.drivex.data.ExpensesRoomDatabase
import com.example.drivex.data.model.Refuel
import com.example.drivex.data.repository.ExpensesRepository
import com.example.drivex.data.repository.ExpensesRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.NumberFormat
import java.util.*

class AbstractViewModel(application: Application) : AndroidViewModel(application) {
    private val expensesRepository: ExpensesRepository

    init {
        expensesRepository = ExpensesRepositoryImpl(application)
    }
    val expenses = MediatorLiveData<List<Refuel>>()
    private val runsSortedByDate = expensesRepository.getAllExpensesBydate()

    init {
        expenses.addSource(runsSortedByDate) { result ->
            Timber.d("RUNS SORTED BY DATE")
            result?.let { expenses.value = it }
        }
    }


    private val refuelDao: ExpensesDao by lazy {
        val db = ExpensesRoomDatabase.getInstance(application)
        db.refuelDao()
    }

    suspend fun readAllDataByDate(): List<Refuel> {
        return expensesRepository.getAllRefuel()
    }

    suspend fun totalRef(key: String): Int {
        return expensesRepository.getSUmExpensesIntById(key)
    }

    fun readRefuelById(id: Long): LiveData<Refuel> {
        return expensesRepository.getRefuelById(id)
    }

    fun addRefuel(refuel: Refuel) {
        viewModelScope.launch(Dispatchers.IO) {
            expensesRepository.addRefuel(refuel)
        }
    }

    fun insert(refuel: Refuel) {
        viewModelScope.launch(Dispatchers.IO) {
            expensesRepository.insert(refuel)
        }
    }

    fun delete(refuel: Refuel) = viewModelScope.launch {
        refuelDao.delete(refuel)
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