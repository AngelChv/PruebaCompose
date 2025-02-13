package com.example.pruebacompose.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
        // Si no se ha selecionado una película se muestra un mensaje:
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No se ha seleccionado ninguna película")
        }
    } else {
        Scaffold(
            topBar = {
                FilmDetailToolbar(
                    film = film,
                    onEditClick = { navigateToEditFilm(film!!) },
                    onDeleteClick = {
                        showDialog = true
                    },
                    navigateBack = navigateBack
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

        ConfirmDeleteFilmDialog(showDialog, onDismissClick = { showDialog = false }) {
            showDialog = false
            viewModel.deleteFilm(
                filmId = film!!.id,
                onSuccess = {
                    Toast.makeText(
                        context, "Película eliminada", Toast.LENGTH_SHORT,
                    ).show()
                    navigateBack()
                },
                onError = {
                    Toast.makeText(
                        context,
                        "Error al eliminar la película",
                        Toast.LENGTH_SHORT,
                    ).show()
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
        title = { Text("Detalle de ${film!!.title}") },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        },
        actions = {
            FilmDetailActions(
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
            )
        }
    )
}

@Composable
fun FilmDetailActions(onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row {
        EditFilmButton(onEditClick)

        DeleteFilmButton(onDeleteClick)
    }
}

@Composable
fun EditFilmButton(onEditClick: () -> Unit) {
    IconButton(
        onClick = onEditClick
    ) {
        Icon(Icons.Default.Edit, "Editar")
    }
}

@Composable
fun DeleteFilmButton(onDeleteClick: () -> Unit) {
    IconButton(
        onClick = onDeleteClick,
    ) {
        Icon(Icons.Default.Delete, "Eliminar")
    }
}

@Composable
fun ConfirmDeleteFilmDialog(
    showDialog: Boolean,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    if (showDialog) AlertDialog(
        title = { Text("¿Desea eliminar la película?") },
        onDismissRequest = onDismissClick,
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text("Si")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FilmDetailsPreview() {
    val viewModel: FilmViewModel = viewModel()
    viewModel.setCurrentFilm(Film.example())
    FilmDetailScreen(viewModel = viewModel, {}, {})
}