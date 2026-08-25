package com.example.pokedex.presentation.pokemonlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pokedex.domain.model.PokemonSummary
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = koinViewModel(),
    onPokemonClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Pokédex") }) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isInitialLoading -> LoadingState()
                state.error != null && state.items.isEmpty() -> ErrorState(
                    message = state.error!!,
                    onRetry = viewModel::retry
                )
                state.items.isEmpty() -> EmptyState()
                else -> PokemonList(
                    items = state.items,
                    isLoadingMore = state.isLoadingMore,
                    onLoadMore = viewModel::loadNextPage,
                    onPokemonClick = onPokemonClick
                )
            }
        }
    }
}

@Composable
private fun PokemonList(
    items: List<PokemonSummary>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onPokemonClick: (Int) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                val total = listState.layoutInfo.totalItemsCount
                if (lastVisibleIndex != null && total > 0 && lastVisibleIndex >= total - 5) {
                    onLoadMore()
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items, key = { _, item -> item.id }) { _, item ->
            PokemonListItem(item = item, onClick = { onPokemonClick(item.id) })
        }
        if (isLoadingMore) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun PokemonListItem(item: PokemonSummary, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
            )
            Column {
                Text(
                    text = "#" + item.id.toString().padStart(3, '0'),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = item.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("There are no Pokémon to display", textAlign = TextAlign.Center)
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Something went wrong", style = MaterialTheme.typography.titleMedium)
            Text(message, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
            Button(onClick = onRetry) { Text("Try again") }
        }
    }
}