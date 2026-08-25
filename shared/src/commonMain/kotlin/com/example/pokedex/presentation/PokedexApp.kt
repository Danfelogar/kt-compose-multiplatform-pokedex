package com.example.pokedex.presentation

import androidx.compose.runtime.Composable
import androidx.navigation3.ui.NavDisplay
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import com.example.pokedex.presentation.navigation.Navigator
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun PokedexApp() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }

    val navigator: Navigator = koinInject()

    NavDisplay(
        backStack = navigator.backStack,
        onBack = {navigator.goBack() },
        entryProvider = koinEntryProvider()
    )
}