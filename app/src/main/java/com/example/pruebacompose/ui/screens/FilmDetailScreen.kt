package com.example.pruebacompose.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.viewmodel.FilmViewModel

@Composable
fun FilmDetailScreen(
    viewModel: FilmViewModel,
    navigateBack: () -> Unit,
    navigateToEditFilm: (Film) -> Unit,
) {
    val context = LocalContext.current
    val film by viewModel.currentFilm.collectAsState()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (film == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No se ha seleccionado ninguna película")
        }
    } else {
        Scaffold(
            topBar = {
                FilmDetailToolbar(
                    film = film,
                    navigateBack = navigateBack,
                    onEditClick = { navigateToEditFilm(film!!) },
                    onDeleteClick = { showDialog = true }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                item {
                    // Póster de la película con degradado y título sobrepuesto
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        AsyncImage(
                            model = film!!.posterPath,
                            contentDescription = "Póster de ${film!!.title}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        // Degradado de transparente a opaco (usando el color de superficie variante)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.surfaceVariant)
                                    )
                                )
                        )
                        // Título de la película sobre el degradado
                        Text(
                            text = film!!.title,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        )
                    }
                }
                item {
                    // Información adicional de la película
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Año: ${film!!.year}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Duración: ${film!!.duration} min",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Director: ${film!!.director}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = film!!.description,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }
        ConfirmDeleteFilmDialog(
            showDialog = showDialog,
            onDismissClick = { showDialog = false }
        ) {
            showDialog = false
            viewModel.deleteFilm(
                filmId = film!!.id,
                onSuccess = {
                    Toast.makeText(context, "Película eliminada", Toast.LENGTH_SHORT).show()
                    navigateBack()
                },
                onError = {
                    Toast.makeText(context, "Error al eliminar la película", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailToolbar(
    film: Film?,
    navigateBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Detalle de ${film?.title ?: ""}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        },
        actions = {
            FilmDetailActions(
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    )
}

@Composable
fun FilmDetailActions(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row {
        EditFilmButton(onEditClick)
        DeleteFilmButton(onDeleteClick)
    }
}

@Composable
fun EditFilmButton(onEditClick: () -> Unit) {
    IconButton(onClick = onEditClick) {
        Icon(Icons.Default.Edit, contentDescription = "Editar")
    }
}

@Composable
fun DeleteFilmButton(onDeleteClick: () -> Unit) {
    IconButton(onClick = onDeleteClick) {
        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
    }
}

@Composable
fun ConfirmDeleteFilmDialog(
    showDialog: Boolean,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            title = { Text("¿Desea eliminar la película?") },
            onDismissRequest = onDismissClick,
            confirmButton = {
                TextButton(onClick = onConfirmClick) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissClick) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilmDetailsPreview() {
    val viewModel: FilmViewModel = viewModel()
    viewModel.setCurrentFilm(Film.example())
    FilmDetailScreen(viewModel = viewModel, {}, {})
}