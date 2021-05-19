package com.example.drivex.presentation.ui.activity.viewModels

import android.app.Application
import androidx.lifecycle.ViewModelProvider

class AbstractViewModelFactory {

     class ViewModelFactory(
         private val aplication: Application
     ) : ViewModelProvider.Factory {
         override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
             return AbstractViewModel(aplication) as T
         }
     }
}