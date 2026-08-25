package com.example.pokedex.data.remote

import com.example.pokedex.data.local.PokemonDao
import com.example.pokedex.data.local.PokemonEntity
import com.example.pokedex.data.remote.dto.PokemonDto
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonStat
import com.example.pokedex.domain.model.PokemonSummary
import com.example.pokedex.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val api: PokemonApiService,
    private val dao: PokemonDao
) : PokemonRepository {

    companion object {
        fun officialArtworkUrl(id: Int) =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    }

    override suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonSummary> {
        return try {
            val response = api.getPokemonList(limit, offset)
            val entities = response.results.map { resource ->
                val id = resource.url.trimEnd('/').substringAfterLast('/').toInt()
                PokemonEntity(id = id, name = resource.name, imageUrl = officialArtworkUrl(id))
            }
            dao.upsertAll(entities)
            entities.map { it.toSummary() }
        } catch (e: Exception) {
            val cached = dao.getPage(limit, offset)
            if (cached.isEmpty()) throw e
            cached.map { it.toSummary() }
        }
    }

    override suspend fun getPokemonDetail(nameOrId: String): Pokemon {
        return try {
            val dto = api.getPokemonDetail(nameOrId)
            dao.upsert(dto.toEntity())
            dto.toDomain()
        } catch (e: Exception) {
            val id = nameOrId.toIntOrNull()
            val cached = id?.let { dao.getById(it) }
            cached?.takeIf { it.isDetailCached }?.toDomain() ?: throw e
        }
    }

    private fun PokemonEntity.toSummary() = PokemonSummary(id, name, imageUrl)

    private fun PokemonEntity.toDomain() = Pokemon(
        id = id, name = name, imageUrl = imageUrl, height = height, weight = weight,
        types = types.split(",").filter { it.isNotBlank() },
        stats = statsCsv.split(",").filter { it.isNotBlank() }.map {
            val (n, v) = it.split(":")
            PokemonStat(n, v.toInt())
        }
    )

    private fun PokemonDto.toEntity() = PokemonEntity(
        id = id, name = name,
        imageUrl = sprites.other?.officialArtwork?.frontDefault ?: sprites.frontDefault ?: officialArtworkUrl(id),
        height = height, weight = weight,
        types = types.sortedBy { it.slot }.joinToString(",") { it.type.name },
        statsCsv = stats.joinToString(",") { "${it.stat.name}:${it.baseStat}" },
        isDetailCached = true
    )

    private fun PokemonDto.toDomain() = Pokemon(
        id = id, name = name,
        imageUrl = sprites.other?.officialArtwork?.frontDefault ?: sprites.frontDefault ?: officialArtworkUrl(id),
        height = height, weight = weight,
        types = types.sortedBy { it.slot }.map { it.type.name },
        stats = stats.map { PokemonStat(it.stat.name, it.baseStat) }
    )

}