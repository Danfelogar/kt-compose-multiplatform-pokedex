package com.example.pokedex.presentation.pokemondetail

import app.cash.turbine.test
import com.example.pokedex.MainDispatcherTestBase
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonStat
import com.example.pokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.pokedex.domain.usecase.ObserveFavoriteIdsUseCase
import com.example.pokedex.domain.usecase.ToggleFavoriteUseCase
import com.example.pokedex.fakes.FakeFavoritesRepository
import com.example.pokedex.fakes.FakePokemonRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PokemonDetailViewModelTest : MainDispatcherTestBase() {

    private fun pokemon(id: Int) = Pokemon(
        id = id, name = "ditto", imageUrl = "url", height = 3, weight = 40,
        types = listOf("normal"), stats = listOf(PokemonStat("hp", 48))
    )

    private fun buildViewModel(
        pokemonRepository: FakePokemonRepository,
        favoritesRepository: FakeFavoritesRepository,
        id: Int = 132
    ) = PokemonDetailViewModel(
        pokemonId = id,
        getPokemonDetail = GetPokemonDetailUseCase(pokemonRepository),
        observeFavoriteIds = ObserveFavoriteIdsUseCase(favoritesRepository),
        toggleFavorite = ToggleFavoriteUseCase(favoritesRepository)
    )

    @Test
    fun loadsDetailSuccessfully() = runTest {
        val repo = FakePokemonRepository().apply { detailResult = Result.success(pokemon(132)) }
        val viewModel = buildViewModel(repo, FakeFavoritesRepository())

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("ditto", state.pokemon?.name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun exposesErrorWhenDetailFailsToLoad() = runTest {
        val repo = FakePokemonRepository().apply { detailResult = Result.failure(RuntimeException("404")) }
        val viewModel = buildViewModel(repo, FakeFavoritesRepository())

        viewModel.uiState.test {
            assertEquals("404", awaitItem().error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun togglingFavoriteUpdatesIsFavorite() = runTest {
        val repo = FakePokemonRepository().apply { detailResult = Result.success(pokemon(132)) }
        val favoritesRepository = FakeFavoritesRepository()
        val viewModel = buildViewModel(repo, favoritesRepository, id = 132)

        assertFalse(viewModel.uiState.value.isFavorite)
        viewModel.onFavoriteClick()
        assertTrue(viewModel.uiState.value.isFavorite)
    }
}