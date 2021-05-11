package com.example.drivex.presentation.ui.stat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Statistic Fragment"
    }
    val text: LiveData<String> = _text
}