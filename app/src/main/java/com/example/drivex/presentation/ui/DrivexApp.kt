package com.example.drivex.presentation.ui

import android.app.Application
import com.example.drivex.di.todoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DrivexApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DrivexApp)
            modules(todoModule)
        }
    }
}