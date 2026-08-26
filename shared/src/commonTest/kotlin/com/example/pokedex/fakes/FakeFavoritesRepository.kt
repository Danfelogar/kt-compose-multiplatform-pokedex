package com.example.pokedex.fakes

import com.example.pokedex.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeFavoritesRepository : FavoritesRepository {
    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())

    override fun observeFavoriteIds(): StateFlow<Set<Int>> = _favoriteIds

    override suspend fun toggleFavorite(id: Int) {
        _favoriteIds.value =
            if (id in _favoriteIds.value) _favoriteIds.value - id else _favoriteIds.value + id
    }
}