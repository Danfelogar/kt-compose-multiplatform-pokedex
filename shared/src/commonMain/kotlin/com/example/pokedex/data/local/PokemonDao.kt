package com.example.pokedex.data.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert

@Dao
interface PokemonDao {
    @Upsert
    suspend fun upsertAll(items: List<PokemonEntity>)

    @Upsert
    suspend fun upsert(item: PokemonEntity)

    @Query("SELECT * FROM pokemon ORDER BY id LIMIT :limit OFFSET :offset")
    suspend fun getPage(limit: Int, offset: Int): List<PokemonEntity>

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getById(id: Int): PokemonEntity?
}