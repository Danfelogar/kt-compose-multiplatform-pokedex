package com.example.pokedex.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NamedApiResourceDto(
    val name: String,
    val url: String
)

@Serializable
data class PokemonListResponseDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NamedApiResourceDto>
)

@Serializable
data class PokemonDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonTypeSlotDto>,
    val stats: List<PokemonStatDto>,
    val sprites: PokemonSpritesDto
)

@Serializable
data class PokemonTypeSlotDto(
    val slot: Int,
    val type: NamedApiResourceDto
)

@Serializable
data class PokemonStatDto(
    @SerialName("base_stat") val baseStat: Int,
    val stat: NamedApiResourceDto
)

@Serializable
data class PokemonSpritesDto(
    @SerialName("front_default") val frontDefault: String? = null,
    val other: PokemonOtherSpritesDto? = null
)

@Serializable
data class PokemonOtherSpritesDto(
    @SerialName("official-artwork") val officialArtwork: OfficialArtworkDto? = null
)

@Serializable
data class OfficialArtworkDto(
    @SerialName("front_default") val frontDefault: String? = null
)