package com.example.drivex.presentation.ui.map

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.drivex.data.model.MapModels
import com.example.drivex.data.repository.MapRepository
import com.example.drivex.utils.SortType
import kotlinx.coroutines.launch
import timber.log.Timber

class MapViewModel @ViewModelInject constructor(
    private val mapRepository: MapRepository
) : ViewModel() {
    var totalAvgSpeed = mapRepository.getTotalAvgSpeed()
    private val runsSortedByDate = mapRepository.getAllDriveSortedByDate()
    private val runsSortedByDistance = mapRepository.getAllDriveSortedByDistance()
    private val runsSortedByTimeInMillis = mapRepository.getAllDriveSortedByTimeInMillis()

    val runs = MediatorLiveData<List<MapModels>>()

    var sortType = SortType.DATE

    /**
     * Posts the correct run list in the LiveData
     */
    init {
        runs.addSource(runsSortedByDate) { result ->
            Timber.d("RUNS SORTED BY DATE")
            if(sortType == SortType.DATE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDistance) { result ->
            if(sortType == SortType.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMillis) { result ->
            if(sortType == SortType.RUNNING_TIME) {
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) = when(sortType) {
        SortType.DATE -> runsSortedByDate.value?.let { runs.value = it }
        SortType.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
        SortType.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }

    fun insertRun(mapModels: MapModels) = viewModelScope.launch {
        mapRepository.insertMap(mapModels)
    }

    fun deleteRun(mapModels: MapModels) = viewModelScope.launch {
        mapRepository.deleteRun(mapModels)
    }
}