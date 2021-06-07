package com.example.drivex.presentation.ui.stat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drivex.data.repository.MapRepository

class StatViewModel @ViewModelInject constructor(
    mapRepository: MapRepository


) : ViewModel() {

    var totalDistance = mapRepository.getTotalDistance()
    var totalTimeInMillis = mapRepository.getTotalTimeInMillis()
    var totalAvgSpeed = mapRepository.getTotalAvgSpeed()
    var runsSortedByDate = mapRepository.getAllDriveSortedByDate()




}