package com.example.pokedex.domain.usecase

import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonSummary
import com.example.pokedex.domain.repository.PokemonRepository

class GetPokemonListUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(limit: Int, offset: Int): List<PokemonSummary> =
        repository.getPokemonList(limit, offset)
}

class GetPokemonDetailUseCase(private val repository: PokemonRepository) {
    suspend operator fun invoke(nameOrId: String): Pokemon =
        repository.getPokemonDetail(nameOrId)
}