package com.example.pruebacompose.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.viewmodel.FilmViewModel
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailScreen(
    navController: NavController,
    viewModel: FilmViewModel
) {
    val film by viewModel.currentFilm.collectAsState()

    if (film == null) {
        // Si no se ha selecionado una película se muestra un mensaje:
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No se ha seleccionado ninguna película")
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle de ${film!!.title}") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    },
                    actions = {
                        FilmDetailActions(
                            navController,
                            film = film!!,
                            vieModel = viewModel
                        )
                    }
                )
            }
        ) { paddingValues: PaddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(text = film!!.title, style = MaterialTheme.typography.titleLarge)
                Text(text = "Año: ${film!!.year}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun FilmDetailActions(
    navController: NavController,
    vieModel: FilmViewModel,
    film: Film,
) {
    Row {
        IconButton(onClick = {
            navController.navigate("editFilmForm/${Json.encodeToString(film)}")
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

@Preview(showBackground = true)
@Composable
fun FilmDetailsPreview() {
    FilmDetailScreen(navController = rememberNavController(), viewModel = viewModel())
}