package com.example.drivex.presentation.ui.activity.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.drivex.domain.ExpensesDao
import com.example.drivex.data.ExpensesRoomDatabase
import com.example.drivex.data.model.Expenses
import com.example.drivex.data.repository.ExpensesRepository
import com.example.drivex.data.repository.ExpensesRepositoryImpl
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SERVICE
import com.example.drivex.utils.Constans.SHOPPING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class AbstractViewModel(application: Application) : AndroidViewModel(application) {
    private val expensesRepository: ExpensesRepository

    init {
        expensesRepository = ExpensesRepositoryImpl(application)
    }
    var filtersExpensesLiveData = MediatorLiveData<List<Expenses>>()

    val expenses = MediatorLiveData<List<Expenses>>()
    private val expensesSortedByDate = expensesRepository.getAllExpensesBydate()

    init {
        expenses.addSource(expensesSortedByDate) { result ->
            result?.let { expenses.value = it }
        }
    }

    fun getExpensesByFilters(filters:ArrayList<String>){
        filtersExpensesLiveData.addSource(expensesSortedByDate) { result ->
            val filteredExpenses = mutableListOf<Expenses>()
            result.forEach {
                filters.forEach { filter->
                    if (it.title == filter)
                        filteredExpenses.add(it)
                }
            }
            filtersExpensesLiveData.value = filteredExpenses
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