package com.example.pruebacompose.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.viewmodel.FilmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailScreen(
    navController: NavController,
    film: Film,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de ${film.title}") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = { FilmDetailActions(navController, film = film) }
            )
        }
    ) { paddingValues: PaddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = film.title, style = MaterialTheme.typography.titleLarge)
            Text(text = "Año: ${film.year}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilmDetailsPreview() {
    val film = Film(
        id = 0,
        title = "Título",
        director = "Director",
        year = 1999,
        duration = 130,
        description = "Descripción",
        posterPath = "poster path"
    )
    FilmDetailScreen(navController = rememberNavController(), film = film)
}

@Composable
fun FilmDetailActions(
    navController: NavController,
    vieModel: FilmViewModel = viewModel(),
    film: Film,
) {
    Row {
        IconButton(onClick = {
            // Todo ir al formulario
            vieModel.updateFilm(
                film = film,
                onSuccess = {},
                onError = {
                    // todo hacer snakbar con el mensaje.
                }
            )
        }) {
            Icon(Icons.Default.Edit, "Editar")
        }

        IconButton(onClick = {
            vieModel.deleteFilm(
                filmId = film.id,
                onSuccess = { navController.popBackStack() },
                onError = {
                    // todo hacer snakbar con el mensaje.
                }
            )
        }) {
            Icon(Icons.Default.Delete, "Eliminar")
        }
    }
}