package com.example.pokedex.fakes

import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonSummary
import com.example.pokedex.domain.repository.PokemonRepository

class FakePokemonRepository : PokemonRepository {
    var listResult: Result<List<PokemonSummary>> = Result.success(emptyList())
    var detailResult: Result<Pokemon> = Result.failure(IllegalStateException("not configured"))
    var callCount = 0
        private set

    override suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonSummary> {
        callCount++
        return listResult.getOrThrow()
    }

    override suspend fun getPokemonDetail(nameOrId: String): Pokemon =
        detailResult.getOrThrow()
}