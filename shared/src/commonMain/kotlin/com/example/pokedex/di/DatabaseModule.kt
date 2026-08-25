package com.example.pokedex.di

import com.example.pokedex.data.local.AppDatabase
import com.example.pokedex.data.local.getRoomDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { getRoomDatabase(get()) }
    single { get<AppDatabase>().favoriteDao() }
    single { get<AppDatabase>().pokemonDao() }
}