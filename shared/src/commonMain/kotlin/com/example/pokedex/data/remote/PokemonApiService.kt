package com.example.pokedex.data.remote

import com.example.pokedex.data.remote.dto.PokemonDto
import com.example.pokedex.data.remote.dto.PokemonListResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PokemonApiService(
    private val client: HttpClient
) {
    private companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/pokemon"
    }

    suspend fun getPokemonList(limit: Int, offset: Int): PokemonListResponseDto =
        client.get(BASE_URL) {
            parameter("limit", limit)
            parameter("offset", offset)
        }.body()

    suspend fun getPokemonDetail(nameOrId: String): PokemonDto =
        client.get("$BASE_URL/$nameOrId").body()
}