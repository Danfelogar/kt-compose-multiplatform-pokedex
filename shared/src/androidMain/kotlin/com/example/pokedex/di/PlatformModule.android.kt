package com.example.pokedex.di

import com.example.pokedex.data.local.getDatabaseBuilder
import com.example.pokedex.data.network.AndroidConnectivityObserver
import com.example.pokedex.domain.network.ConnectivityObserver
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { getDatabaseBuilder(androidContext()) }
    single<ConnectivityObserver> { AndroidConnectivityObserver(androidContext()) }
}