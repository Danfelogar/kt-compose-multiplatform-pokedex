package com.example.pokedex.presentation.pokemondetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.pokedex.domain.model.Pokemon

@Composable
fun PokemonDetailScreen(
    viewModel: PokemonDetailViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.pokemon?.name?.replaceFirstChar { it.uppercase() } ?: "Pokémon") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (state.pokemon != null) {
                        IconButton(onClick = viewModel::onFavoriteClick) {
                            Icon(
                                imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (state.isFavorite) "Quitar de favoritos" else "Marcar como favorito"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                state.error != null -> Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Algo salió mal", style = MaterialTheme.typography.titleMedium)
                        Text(state.error!!, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 12.dp))
                        Button(onClick = viewModel::retry) { Text("Reintentar") }
                    }
                }
                state.pokemon != null -> PokemonDetailContent(state.pokemon!!)
            }
        }
    }
}

@Composable
private fun PokemonDetailContent(pokemon: Pokemon) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(160.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
            )
        }
        item {
            Text(
                text = "#" + pokemon.id.toString().padStart(3, '0'),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        item {
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                pokemon.types.forEach { type ->
                    AssistChip(
                        onClick = {},
                        label = { Text(type.replaceFirstChar { it.uppercase() }) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
        item {
            Text(
                text = "Altura: ${pokemon.height / 10.0} m   ·   Peso: ${pokemon.weight / 10.0} kg",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        item {
            Text("Estadísticas base", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
        }
        items(pokemon.stats) { stat ->
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Text(stat.name.replace("-", " ").replaceFirstChar { it.uppercase() })
                    Text(stat.baseStat.toString())
                }
                LinearProgressIndicator(
                    progress = { (stat.baseStat / 200f).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                )
            }
        }
    }
}