package com.example.drivex.presentation.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.data.model.Refuel
import com.example.drivex.data.repository.RefuelRepository
import com.example.drivex.data.repository.RefuelRepositoryImpl
import com.example.drivex.presentation.adapters.MainAdapter

class HomeViewModel(application: Application) : ViewModel() {

    private val refuelRepository:RefuelRepository
    init {
        refuelRepository = RefuelRepositoryImpl(application)
    }

    suspend fun readAllDataByDate(): List<Refuel> {
        return refuelRepository.getAllRefuel()
    }

}

