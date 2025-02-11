package com.example.pruebacompose.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.viewmodel.FilmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(
    navController: NavController,
    // El valor por defecto utiliza la funcion viewModel() que automáticamente devuelve
    // una instancia de la clase indicada, o bien en el tipo genérico <> o en la definición
    // de la variable.
    viewModel: FilmViewModel = viewModel(),
    onFilmClick: (Film) -> Unit,
) {
    // Obtenemos la lista de películas desde el ViewModel.
    val films by viewModel.films.collectAsState()

    // Cargamos las películas cuando se muestra la pantalla.
    LaunchedEffect(Unit) {
        viewModel.loadFilms()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Películas") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addFilm") },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Película")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            // Usamos el import correcto de items para iterar la lista de películas.
            items(films) { film ->
                FilmItem(
                    film = film,
                    onFilmClick = onFilmClick
                )
            }
        }
    }
}

@Composable
fun FilmItem(film: Film, onFilmClick: (Film) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onFilmClick(film) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Se usan tipografías de Material3: titleLarge en lugar de h6
            Text(text = film.title, style = MaterialTheme.typography.titleLarge)
            // Se usa bodyMedium en lugar de body2
            Text(text = "Año: ${film.year}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
