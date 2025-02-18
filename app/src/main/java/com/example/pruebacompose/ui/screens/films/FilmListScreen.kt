package com.example.pruebacompose.ui.screens.films

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.pruebacompose.core.navigation.BottomNavBar
import com.example.pruebacompose.ui.components.CreateFilmFab
import com.example.pruebacompose.domain.model.Film
import com.example.pruebacompose.data.remote.ApiClient
import com.example.pruebacompose.data.repository.FilmRepository
import com.example.pruebacompose.data.service.FilmService
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

    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }

    val filteredFilms = films.filter {
        it.title.contains(query, true) || it.director.contains(query, true)
    }

    // Cargamos las películas cuando se muestra la pantalla.
    LaunchedEffect(Unit) {
        viewModel.loadFilms()
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            if (isSearchActive) {
                TextField(value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Buscar películas...") },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { query = ""; isSearchActive = false }) {
                            Icon(
                                Icons.Default.Close, contentDescription = "Cerrar búsqueda"
                            )
                        }
                    })
            } else {
                Text("Películas")
            }
        }, actions = {
            if (!isSearchActive) {
                IconButton(onClick = { isSearchActive = true }) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            }
        })
    },
        bottomBar = bottomNavBar,
        floatingActionButton = { CreateFilmFab { navigateToCreateFilm() } }) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues),
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Usamos el import correcto de items para iterar la lista de películas.
            items(filteredFilms.size) { index ->
                FilmItem(
                    modifier = Modifier.aspectRatio(2 / 3f),
                    film = filteredFilms[index],
                    onFilmClick = onFilmClick
                )
            }
        }
    }
}

@Composable
fun FilmItem(modifier: Modifier, film: Film, onFilmClick: (Film) -> Unit) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant

    ElevatedCard(
        modifier = modifier,
        onClick = { onFilmClick(film) },
    ) {
        AsyncImage(
            model = film.posterPath ?: film.defaultPoster(
                isSystemInDarkTheme(),
                backgroundColor = backgroundColor,
                textColor = textColor,
            ),
            contentDescription = "Poster de la película",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
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
        FilmItem(Modifier, Film.example()) { }
    }
}