package com.example.pokedex.domain.model

data class PokemonSummary(
    val id: Int,
    val name: String,
    val imageUrl: String
)

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Int,
    val weight: Int,
    val types: List<String>,
    val stats: List<PokemonStat>
)

data class PokemonStat(
    val name: String,
    val baseStat: Int
)