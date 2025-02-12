package com.example.pruebacompose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.models.FilmCreate
import com.example.pruebacompose.viewmodel.FilmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmFormScreen(
    viewModel: FilmViewModel, navController: NavController, film: Film? = null
) {
    var title by remember { mutableStateOf(film?.title ?: "") }
    var director by remember { mutableStateOf(film?.director ?: "") }
    var year by remember { mutableStateOf("${film?.year ?: ""}") }
    var duration by remember { mutableStateOf("${film?.duration ?: ""}") }
    var description by remember { mutableStateOf(film?.description ?: "") }
    var posterPath by remember { mutableStateOf(film?.posterPath ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isEditing = film != null

    fun validateInt(value: String): Boolean {
        return value.isNotBlank() && value.toIntOrNull() != null
    }

    fun validate(): Boolean {
        return title.isNotBlank() && director.isNotBlank() &&
                description.isNotBlank() && validateInt(year) && validateInt(duration)
    }

    fun onSubmitForm() {
        if (validate()) {
            val filmCreate = FilmCreate(title = title,
                director = director,
                year = year.toInt(),
                duration = duration.toInt(),
                description = description,
                posterPath = posterPath.takeIf { it.isNotBlank() })

            if (isEditing) {
                // Puedo usar !! de manera segura porque si isEditing es true
                // film no es nulo.
                viewModel.updateFilm(filmCreate.toFilm(film!!.id),
                    onSuccess = { navController.popBackStack() },
                    onError = { errorMessage = it })
            } else {
                viewModel.createFilm(filmCreate,
                    onSuccess = { navController.popBackStack() },
                    onError = { errorMessage = it })
            }
        } else {
            errorMessage = "Todos los campos son obligatorios excepto el póster."
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                if (isEditing) "Editar película"
                else "Crear Película"
            )
        }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = ::onSubmitForm, modifier = Modifier.padding(16.dp)
        ) {
            if (isEditing) Icon(Icons.Default.Edit, contentDescription = "Editar")
            else Icon(Icons.Default.Add, contentDescription = "Crear")
        }
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = title,
                onValueChange = { title = it },
                label = { Text("Título") })
            OutlinedTextField(value = director,
                onValueChange = { director = it },
                label = { Text("Director") })
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Año") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = duration,
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
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditFilmFormPreview() {
    FilmFormScreen(
        navController = rememberNavController(),
        film = Film.example(),
        viewModel = viewModel()
    )
}

@Preview(showBackground = true)
@Composable
fun CreateFilmFormPreview() {
    FilmFormScreen(
        navController = rememberNavController(),
        viewModel = viewModel()
    )
}
