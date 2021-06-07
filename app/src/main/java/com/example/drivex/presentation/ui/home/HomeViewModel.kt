package com.example.drivex.presentation.ui.home

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.drivex.data.model.Refuel
import com.example.drivex.data.repository.RefuelRepository
import com.example.drivex.data.repository.RefuelRepositoryImpl

class HomeViewModel(application: Application) : ViewModel() {

    private val refuelRepository:RefuelRepository
    init {
        refuelRepository = RefuelRepositoryImpl(application)
    }

    suspend fun readAllDataByDate(): List<Refuel> {
        return refuelRepository.getAllRefuel()
    }

}

