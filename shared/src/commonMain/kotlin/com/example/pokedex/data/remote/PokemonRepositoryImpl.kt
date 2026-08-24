package com.example.pokedex.data.remote

import com.example.pokedex.data.remote.dto.PokemonDto
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonStat
import com.example.pokedex.domain.model.PokemonSummary
import com.example.pokedex.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val api: PokemonApiService
): PokemonRepository {

    companion object {
        fun officialArtworkUrl(id: Int) =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    }

    override suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonSummary> {
        val response = api.getPokemonList(limit, offset)
        return response.results.map { response ->
            val id = response.url.trimEnd('/').substringAfterLast('/').toInt()
            PokemonSummary(
                id = id,
                name = response.name,
                imageUrl = officialArtworkUrl(id)
            )
        }
    }

    override suspend fun getPokemonDetail(nameOrId: String): Pokemon {
        return api.getPokemonDetail(nameOrId).toDomain()
    }

    private fun PokemonDto.toDomain(): Pokemon = Pokemon(
        id = id,
        name = name,
        imageUrl = sprites.other?.officialArtwork?.frontDefault
            ?: sprites.frontDefault
            ?: officialArtworkUrl(id),
        height = height,
        weight = weight,
        types = types.sortedBy { it.slot }.map { it.type.name },
        stats = stats.map { PokemonStat(name = it.stat.name, baseStat = it.baseStat) }
    )

}