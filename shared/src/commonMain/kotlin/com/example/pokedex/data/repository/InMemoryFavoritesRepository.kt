package com.example.pokedex.data.repository

import com.example.pokedex.data.local.FavoriteDao
import com.example.pokedex.data.local.FavoriteEntity
import com.example.pokedex.domain.repository.FavoritesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class FavoritesRepositoryImpl(
    private val dao: FavoriteDao
) : FavoritesRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val favoriteIds: StateFlow<Set<Int>> = dao.observeAll()
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(scope, SharingStarted.Eagerly, emptySet())

    override fun observeFavoriteIds(): StateFlow<Set<Int>> = favoriteIds

    override suspend fun toggleFavorite(id: Int) {
        if (dao.exists(id)) dao.delete(id) else dao.insert(FavoriteEntity(id))
    }
}