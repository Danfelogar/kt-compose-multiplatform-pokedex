package com.example.pokedex.presentation.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.PokemonSummary
import com.example.pokedex.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val getPokemonList: GetPokemonListUseCase
): ViewModel() {
    data class UiState(
        val isLoading: Boolean = false,
        val pokemon: List<PokemonSummary> = emptyList(),
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadFirstPage()
    }

    private fun loadFirstPage() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            runCatching { getPokemonList(limit = 20, offset = 0) }
                .onSuccess { list -> _uiState.value = UiState(pokemon = list) }
                .onFailure { e -> _uiState.value = UiState(error = e.message ?: "Unknow Error") }
        }
    }

}