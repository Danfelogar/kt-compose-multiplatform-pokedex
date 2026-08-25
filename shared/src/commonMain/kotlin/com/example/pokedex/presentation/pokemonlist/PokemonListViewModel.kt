package com.example.pokedex.presentation.pokemonlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.PokemonSummary
import com.example.pokedex.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val getPokemonList: GetPokemonListUseCase
) : ViewModel() {

    data class UiState(
        val items: List<PokemonSummary> = emptyList(),
        val isInitialLoading: Boolean = false,
        val isLoadingMore: Boolean = false,
        val endReached: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var offset = 0
    private val pageSize = 20

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isInitialLoading || state.isLoadingMore || state.endReached) return

        val isFirstPage = state.items.isEmpty()
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isInitialLoading = isFirstPage,
                    isLoadingMore = !isFirstPage,
                    error = null
                )
            }
            runCatching { getPokemonList(limit = pageSize, offset = offset) }
                .onSuccess { newItems ->
                    offset += pageSize
                    _uiState.update {
                        it.copy(
                            items = it.items + newItems,
                            isInitialLoading = false,
                            isLoadingMore = false,
                            endReached = newItems.size < pageSize
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isInitialLoading = false,
                            isLoadingMore = false,
                            error = e.message ?: "The Pokémon list could not be loaded"
                        )
                    }
                }
        }
    }

    fun retry() = loadNextPage()
}