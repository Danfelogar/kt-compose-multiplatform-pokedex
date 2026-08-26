package com.example.pokedex.presentation.pokemonlist

import app.cash.turbine.test
import com.example.pokedex.MainDispatcherTestBase
import com.example.pokedex.domain.model.PokemonSummary
import com.example.pokedex.domain.usecase.GetPokemonListUseCase
import com.example.pokedex.fakes.FakePokemonRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PokemonListViewModelTest : MainDispatcherTestBase() {

    private fun pokemon(id: Int) = PokemonSummary(id, "pokemon-$id", "url-$id")

    @Test
    fun loadsFirstPageOnInit() = runTest {
        val repository = FakePokemonRepository().apply {
            listResult = Result.success((1..20).map(::pokemon))
        }
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(repository))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(20, state.items.size)
            assertFalse(state.isInitialLoading)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun exposesErrorAndKeepsListEmptyWhenFirstPagaFails() = runTest {
        val repository = FakePokemonRepository().apply {
            listResult = Result.failure(RuntimeException("sin conexión"))
        }
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(repository))

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.items.isEmpty())
            assertEquals("sin conexión", state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loadNextPageIsIgnoredOnceEndOfListIsReached() = runTest {
        val repository = FakePokemonRepository().apply {
            listResult = Result.success((1..5).map(::pokemon)) // < pageSize -> última página
        }
        val viewModel = PokemonListViewModel(GetPokemonListUseCase(repository))
        val callsAfterInit = repository.callCount

        viewModel.loadNextPage()
        viewModel.loadNextPage()

        assertEquals(callsAfterInit, repository.callCount) // el guard evitó llamadas de más
    }
}