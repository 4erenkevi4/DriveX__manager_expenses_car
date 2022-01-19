package com.example.drivex.presentation.ui.activity.viewModels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.drivex.domain.ExpensesDao
import com.example.drivex.data.ExpensesRoomDatabase
import com.example.drivex.data.model.Expenses
import com.example.drivex.data.repository.ExpensesRepository
import com.example.drivex.data.repository.ExpensesRepositoryImpl
import com.example.drivex.presentation.ui.dialogs.SettingsDialog
import com.example.drivex.presentation.ui.setting.SettingFragment
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SERVICE
import com.example.drivex.utils.Constans.SHOPPING
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

    val allExpensesSum: LiveData<Double> = refuelDao.getSumOfExpenses()
    val allFuelCostSum: LiveData<Int> = refuelDao.getSUmExpensesById(REFUEL)
    val allServiceCostSum: LiveData<Int> = refuelDao.getSUmExpensesById(SERVICE)
    val refuelSum: LiveData<Int> = refuelDao.getSUmExpensesById(REFUEL)
    val serviceSum: LiveData<Int> = refuelDao.getSUmExpensesById(SERVICE)
    val shoppingSum: LiveData<Int> = refuelDao.getSUmExpensesById(SHOPPING)
    val paymentsSum: LiveData<Int> = refuelDao.getSUmExpensesById(PAYMENT)
    val allFuelVolumeSum: LiveData<Int> = refuelDao.getSummVolumeById(REFUEL)
    val lastMileage: LiveData<Int> = refuelDao.getLastMileage()
    val lastMileageStr: LiveData<String> = Transformations.map(lastMileage) { checkpoint ->
        (checkpoint?.toString() ?: "0")
    }
}