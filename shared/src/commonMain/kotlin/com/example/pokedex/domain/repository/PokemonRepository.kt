package com.example.pokedex.domain.repository

import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonSummary

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonSummary>
    suspend fun getPokemonDetail(nameOrId: String): Pokemon
}