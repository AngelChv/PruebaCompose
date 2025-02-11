package com.example.pruebacompose.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pruebacompose.models.Film

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailScreen(film: Film) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de ${film.title}")}
            )
        }
    ) { paddingValues: PaddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text(text = film.title, style = MaterialTheme.typography.titleLarge)
            Text(text = "Año: ${film.year}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}