package com.example.pokedex.presentation.pokemondetail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PokemonDetailViewModel(
    private val pokemonId: Int
) : ViewModel() {
    data class UiState(
        val isLoading: Boolean = false,
        val name: String? = null,
        val isFavorite: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
}