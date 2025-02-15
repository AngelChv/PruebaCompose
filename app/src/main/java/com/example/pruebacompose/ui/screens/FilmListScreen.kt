package com.example.pruebacompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.pruebacompose.core.navigation.BottomNavBar
import com.example.pruebacompose.core.ui.CreateFilmFab
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.network.ApiClient
import com.example.pruebacompose.repository.FilmRepository
import com.example.pruebacompose.service.FilmService
import com.example.pruebacompose.ui.theme.PruebaComposeTheme
import com.example.pruebacompose.viewmodel.FilmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmListScreen(
    // El valor por defecto utiliza la funcion viewModel() que automáticamente devuelve
    // una instancia de la clase indicada, o bien en el tipo genérico <> o en la definición
    // de la variable.
    // Ya no utilizo viewModel(), lo paso manualmente.
    viewModel: FilmViewModel,
    bottomNavBar: @Composable () -> Unit,
    navigateToCreateFilm: () -> Unit,
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
        bottomBar = bottomNavBar,
        floatingActionButton = { CreateFilmFab { navigateToCreateFilm() } }
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
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = { onFilmClick(film) },
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = film.posterPath ?: film.defaultPoster(
                    isSystemInDarkTheme(),
                    backgroundColor = backgroundColor,
                    textColor = textColor,
                ),
                contentDescription = "Poster de la película",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
            ) {
                Text(
                    text = film.title,
                    color = textColor,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun FilmListPreview() {
    val viewModel =
        FilmViewModel(FilmRepository(ApiClient.retrofit.create(FilmService::class.java)))
    viewModel.setFilms(Film.listExample(10))
    PruebaComposeTheme {
        FilmListScreen(
            viewModel = viewModel,
            bottomNavBar = { BottomNavBar(rememberNavController()) },
            navigateToCreateFilm = { },
        ) { }
    }
}

@Preview(showBackground = true)
@Composable
fun FilmItemPreview() {
    PruebaComposeTheme {
        FilmItem(Film.example()) { }
    }
}