package com.example.drivex.presentation.ui.activity.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.drivex.domain.ExpensesDao
import com.example.drivex.data.ExpensesRoomDatabase
import com.example.drivex.data.model.Expenses
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
    val expenses = MediatorLiveData<List<Expenses>>()
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

    suspend fun readAllDataByDate(): List<Expenses> {
        return expensesRepository.getAllRefuel()
    }

    suspend fun totalRef(key: String): Int {
        return expensesRepository.getSUmExpensesIntById(key)
    }

    fun readRefuelById(id: Long): LiveData<Expenses> {
        return expensesRepository.getRefuelById(id)
    }

    fun addRefuel(expenses: Expenses) {
        viewModelScope.launch(Dispatchers.IO) {
            expensesRepository.addRefuel(expenses)
        }
    }

    fun insert(expenses: Expenses) {
        viewModelScope.launch(Dispatchers.IO) {
            expensesRepository.insert(expenses)
        }
    }

    fun delete(expenses: Expenses) = viewModelScope.launch {
        refuelDao.delete(expenses)
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