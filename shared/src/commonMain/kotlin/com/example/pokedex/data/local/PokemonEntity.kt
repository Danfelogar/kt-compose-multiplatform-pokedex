package com.example.pokedex.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Int = 0,
    val weight: Int = 0,
    val types: String = "",
    val statsCsv: String = "",
    val isDetailCached: Boolean = false
)