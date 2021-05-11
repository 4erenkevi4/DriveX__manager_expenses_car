package com.example.drivex.di

import com.example.drivex.data.repository.RefuelRepository
import com.example.drivex.data.repository.RefuelRepositoryImpl

import org.koin.dsl.module

val todoModule = module {
    single<RefuelRepository> { RefuelRepositoryImpl(get()) }

}





