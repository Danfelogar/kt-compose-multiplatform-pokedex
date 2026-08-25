package com.example.pokedex.presentation.pokemondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.pokedex.domain.usecase.ObserveFavoriteIdsUseCase
import com.example.pokedex.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val pokemonId: Int,
    private val getPokemonDetail: GetPokemonDetailUseCase,
    private val observeFavoriteIds: ObserveFavoriteIdsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = false,
        val pokemon: Pokemon? = null,
        val isFavorite: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        load()
        observeFavoriteIds()
            .onEach { ids -> _uiState.update { it.copy(isFavorite = pokemonId in ids) } }
            .launchIn(viewModelScope)
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { getPokemonDetail(pokemonId.toString()) }
                .onSuccess { pokemon -> _uiState.update { it.copy(isLoading = false, pokemon = pokemon) } }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, error = e.message ?: "The Pokémon could not be loaded")
                    }
                }
        }
    }

    fun onFavoriteClick() {
        viewModelScope.launch { toggleFavorite(pokemonId) }
    }

    fun retry() = load()
}