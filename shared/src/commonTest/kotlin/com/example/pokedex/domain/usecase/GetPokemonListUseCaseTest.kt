package com.example.pokedex.domain.usecase

import com.example.pokedex.domain.model.PokemonSummary
import com.example.pokedex.fakes.FakePokemonRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetPokemonListUseCaseTest {

    @Test
    fun delegatesToRepositoryAndReturnsItsResult() = runTest {
        val expected = listOf(PokemonSummary(id = 1, name = "bulbasaur", imageUrl = "url"))
        val repository = FakePokemonRepository().apply { listResult = Result.success(expected) }
        val useCase = GetPokemonListUseCase(repository)

        val result = useCase(limit = 20, offset = 0)

        assertEquals(expected, result)
        assertEquals(1, repository.callCount)
    }

    @Test
    fun propagatesRepositoryFailure() = runTest {
        val repository = FakePokemonRepository().apply {
            listResult = Result.failure(RuntimeException("network down"))
        }
        val useCase = GetPokemonListUseCase(repository)

        val error = runCatching { useCase(limit = 20, offset = 0) }.exceptionOrNull()

        assertEquals("network down", error?.message)
    }
}