package com.example.pokedex.di

import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test

class AppModuleTest {
    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `koin starts without errors`() {
        initKoin()
    }
}