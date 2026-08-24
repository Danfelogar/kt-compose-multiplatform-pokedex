package com.example.pokedex.presentation.navigation

import androidx.compose.runtime.mutableStateListOf

class Navigator(startDestination: Route) {
    val backStack = mutableStateListOf<Route>(startDestination)

    fun goTo(route: Route) {
        backStack.add(route)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }
}