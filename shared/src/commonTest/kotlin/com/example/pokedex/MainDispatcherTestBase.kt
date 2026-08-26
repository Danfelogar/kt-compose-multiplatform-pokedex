package com.example.pokedex

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * Reemplaza Dispatchers.Main por un test dispatcher, funciona en JVM (Android) e iOS
 * a diferencia de un JUnit4 Rule.
 *
 * Usamos UnconfinedTestDispatcher (no StandardTestDispatcher): las corrutinas lanzadas
 * en el init{} de un ViewModel (como la carga inicial) corren de forma inmediata/eager,
 * así no hace falta llamar advanceUntilIdle() manualmente en cada test.
 */
abstract class MainDispatcherTestBase {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUpMainDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDownMainDispatcher() {
        Dispatchers.resetMain()
    }
}