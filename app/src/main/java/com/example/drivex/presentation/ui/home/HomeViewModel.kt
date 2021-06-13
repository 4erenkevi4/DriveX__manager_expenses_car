package com.example.drivex.presentation.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.drivex.data.model.Refuel
import com.example.drivex.data.repository.ExpensesRepository
import com.example.drivex.data.repository.ExpensesRepositoryImpl

class HomeViewModel(application: Application) : ViewModel() {

    private val refuelRepository:ExpensesRepository
    init {
        refuelRepository = ExpensesRepositoryImpl(application)
    }

    suspend fun readAllDataByDate(): List<Refuel> {
        return refuelRepository.getAllRefuel()
    }

}

