package com.example.pokedex.domain.usecase

import com.example.pokedex.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.StateFlow

class ObserveFavoriteIdsUseCase(private val repository: FavoritesRepository) {
    operator fun invoke(): StateFlow<Set<Int>> = repository.observeFavoriteIds()
}

class ToggleFavoriteUseCase(private val repository: FavoritesRepository) {
    suspend operator fun invoke(id: Int) = repository.toggleFavorite(id)
}