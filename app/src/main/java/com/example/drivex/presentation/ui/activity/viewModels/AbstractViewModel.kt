package com.example.drivex.presentation.ui.activity.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.drivex.domain.ExpensesDao
import com.example.drivex.data.ExpensesRoomDatabase
import com.example.drivex.data.model.Expenses
import com.example.drivex.data.repository.ExpensesRepository
import com.example.drivex.data.repository.ExpensesRepositoryImpl
import com.example.drivex.utils.Constans
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SERVICE
import com.example.drivex.utils.Constans.SHOPPING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AbstractViewModel(application: Application) : AndroidViewModel(application) {
    private val expensesRepository: ExpensesRepository

    init {
        expensesRepository = ExpensesRepositoryImpl(application)
    }

    var filtersExpensesLiveData = MediatorLiveData<List<Expenses>>()


    var sortedExpensesLiveData = MediatorLiveData<List<Expenses>>()


    val expenses = MediatorLiveData<List<Expenses>>()
    private val getAllexpenses = expensesRepository.getAllExpensesBydate()

    init {
        expenses.addSource(getAllexpenses) { result ->
            result?.let { expenses.value = it }
        }
    }

    fun getExpensesByFilters(filters: ArrayList<String>) {
        val filtersPeriod = getPeriodOfFilter(filters)
        var source = getAllexpenses
        if (filtersPeriod != null) {
            source = expensesRepository.getAllExpensesByPeriod(
                filtersPeriod
            )
        }
        filtersExpensesLiveData.addSource(
            source
        ) { result ->
            val filteredExpenses = mutableListOf<Expenses>()
            result.forEach {
                filters.forEach { filter ->
                    if (filtersPeriod != null && filters.size == 1)
                        filteredExpenses.add(it)
                    else if (it.title == filter)
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

    @SuppressLint("CommitPrefEdits")
    fun saveToSP(
        string: String? = null,
        keyType: String,
        value: Boolean = false,
        set: Set<String>? = null,
        prefs: SharedPreferences,
        floatValue: Float? = null
    ) {
        val editor: SharedPreferences.Editor = prefs.edit()
        when {
            floatValue != null -> editor.putFloat(keyType, floatValue)
            string != null -> editor.putString(keyType, string)
            set != null -> editor.putStringSet(keyType, set)
            else -> editor.putBoolean(keyType, value)
        }
        editor.apply()
    }

    fun convertStringToDate(stringDate: String): Long? {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        return sdf.parse(stringDate).time
    }

    fun getMonthOrYear(stringDate: String, isMonth: Boolean): Int {
        val date = convertStringToDate(stringDate)
        return if (isMonth)
            SimpleDateFormat("MM").format(date).toInt()
        else
            SimpleDateFormat("yyyy").format(date).toInt()
    }


    private fun getPeriodOfFilter(filters: ArrayList<String>): Long? {
        val periodsCalendar = Calendar.getInstance()
        val currentCalendar = Calendar.getInstance()
        var period: Long? = null
        if (filters.contains(Constans.PERIOD_ALL))
            period = periodsCalendar.timeInMillis
        if (filters.contains(Constans.PERIOD_DAY)) {
            periodsCalendar.set(
                Calendar.DAY_OF_MONTH,
                currentCalendar.get(Calendar.DAY_OF_MONTH) - 1
            )
            period = periodsCalendar.timeInMillis
        }
        if (filters.contains(Constans.PERIOD_WEEK)) {
            periodsCalendar.set(
                Calendar.WEEK_OF_MONTH,
                currentCalendar.get(Calendar.WEEK_OF_MONTH) - 1
            )
            period = periodsCalendar.timeInMillis
        }
        if (filters.contains(Constans.PERIOD_MOUNTH)) {
            periodsCalendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH) - 1)
            period = periodsCalendar.timeInMillis
        }
        if (filters.contains(Constans.PERIOD_THREE_MOUNTH)) {
            periodsCalendar.set(Calendar.MONTH, currentCalendar.get(Calendar.MONTH) - 3)
            period = periodsCalendar.timeInMillis
        }
        if (filters.contains(Constans.PERIOD_YEAR)) {
            periodsCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR) - 1)
            period = periodsCalendar.timeInMillis
        }
        return period
    }
}