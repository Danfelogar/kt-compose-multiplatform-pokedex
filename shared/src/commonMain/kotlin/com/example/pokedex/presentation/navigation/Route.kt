package com.example.pokedex.presentation.navigation

import androidx.navigation3.runtime.NavKey

sealed interface  Route: NavKey
data object PokemonListRoute: Route
data class PokemonDetailRoute(val pokemonId: Int): Route