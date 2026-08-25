package com.example.pokedex.di

import com.example.pokedex.data.remote.PokemonApiService
import com.example.pokedex.data.remote.PokemonRepositoryImpl
import com.example.pokedex.data.remote.createHttpClient
import com.example.pokedex.data.repository.FavoritesRepositoryImpl
import com.example.pokedex.domain.repository.FavoritesRepository
import com.example.pokedex.domain.repository.PokemonRepository
import com.example.pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.pokedex.domain.usecase.GetPokemonListUseCase
import com.example.pokedex.domain.usecase.ObserveFavoriteIdsUseCase
import com.example.pokedex.domain.usecase.ToggleFavoriteUseCase
import org.koin.dsl.module

val dataModule = module {
    single { createHttpClient() }
    single { PokemonApiService(get()) }
    single<PokemonRepository> { PokemonRepositoryImpl(get(), get()) }
    factory { GetPokemonListUseCase(get()) }
    factory { GetPokemonDetailUseCase(get()) }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
    factory { ObserveFavoriteIdsUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
}