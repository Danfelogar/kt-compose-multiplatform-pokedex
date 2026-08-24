package com.example.pokedex.di

import com.example.pokedex.data.remote.PokemonApiService
import com.example.pokedex.data.remote.PokemonRepositoryImpl
import com.example.pokedex.data.remote.createHttpClient
import com.example.pokedex.domain.repository.PokemonRepository
import com.example.pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.pokedex.domain.usecase.GetPokemonListUseCase
import org.koin.dsl.module

val dataModule = module {
    single { createHttpClient() }
    single { PokemonApiService(get()) }
    single<PokemonRepository> { PokemonRepositoryImpl(get()) }
    factory { GetPokemonListUseCase(get()) }
    factory { GetPokemonDetailUseCase(get()) }
}