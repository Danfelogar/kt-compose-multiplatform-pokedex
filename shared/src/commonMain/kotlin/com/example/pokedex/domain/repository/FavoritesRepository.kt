package com.example.pokedex.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface FavoritesRepository {
    fun observeFavoriteIds(): StateFlow<Set<Int>>
    suspend fun toggleFavorite(id: Int)
}