package com.example.pokedex.di

import com.example.pokedex.presentation.navigation.Navigator
import com.example.pokedex.presentation.navigation.PokemonDetailRoute
import com.example.pokedex.presentation.navigation.PokemonListRoute
import com.example.pokedex.presentation.pokemondetail.PokemonDetailScreen
import com.example.pokedex.presentation.pokemondetail.PokemonDetailViewModel
import com.example.pokedex.presentation.pokemonlist.PokemonListScreen
import com.example.pokedex.presentation.pokemonlist.PokemonListViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val presentationModule = module {
    single { Navigator(startDestination = PokemonListRoute) }

    viewModel { PokemonListViewModel(get()) }
    viewModel { params -> PokemonDetailViewModel(pokemonId = params.get()) }

    navigation<PokemonListRoute> {
        val navigator = get<Navigator>()
        PokemonListScreen( onPokemonClick =  { id -> navigator.goTo(PokemonDetailRoute(id)) } )
    }

    navigation<PokemonDetailRoute> { route ->
        val navigator = get<Navigator>()
        PokemonDetailScreen(
            viewModel = get { parametersOf(route.pokemonId) },
            onBack = { navigator.goBack() }
        )
    }
}