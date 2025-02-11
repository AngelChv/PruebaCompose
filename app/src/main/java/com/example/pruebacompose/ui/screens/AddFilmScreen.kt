package com.example.pruebacompose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pruebacompose.models.FilmCreate
import com.example.pruebacompose.viewmodel.FilmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFilmScreen(
    viewModel: FilmViewModel = viewModel(), navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var posterPath by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Añadir Película") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(value = title,
                onValueChange = { title = it },
                label = { Text("Título") })
            OutlinedTextField(value = director,
                onValueChange = { director = it },
                label = { Text("Director") })
            OutlinedTextField(value = year,
                onValueChange = { year = it },
                label = { Text("Año") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(value = duration,
                onValueChange = { duration = it },
                label = { Text("Duración (min)") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") })
            OutlinedTextField(value = posterPath,
                onValueChange = { posterPath = it },
                label = { Text("URL del Poster (opcional)") })

            errorMessage?.let { Text(it, color = Color.Red) }

            Button(
                onClick = {
                    if (title.isNotBlank() && director.isNotBlank() && year.isNotBlank() && duration.isNotBlank() && description.isNotBlank()) {
                        viewModel.createFilm(FilmCreate(title = title,
                            director = director,
                            year = year.toInt(),
                            duration = duration.toInt(),
                            description = description,
                            posterPath = posterPath.takeIf { it.isNotBlank() }),
                            onSuccess = { navController.popBackStack() },
                            onError = { errorMessage = it })
                    } else {
                        errorMessage = "Todos los campos son obligatorios excepto el póster."
                    }
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Película")
            }
        }
    }
}