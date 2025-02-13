package com.example.pruebacompose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pruebacompose.models.Film
import com.example.pruebacompose.models.FilmCreate
import com.example.pruebacompose.network.ApiClient
import com.example.pruebacompose.repository.FilmRepository
import com.example.pruebacompose.service.FilmService
import com.example.pruebacompose.viewmodel.FilmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmFormScreen(
    viewModel: FilmViewModel,
    navigateBack: () -> Unit,
) {
    val film by viewModel.currentFilm.collectAsState()

    var title by rememberSaveable { mutableStateOf(film?.title ?: "") }
    var director by rememberSaveable { mutableStateOf(film?.director ?: "") }
    var year by rememberSaveable { mutableStateOf("${film?.year ?: ""}") }
    var duration by rememberSaveable { mutableStateOf("${film?.duration ?: ""}") }
    var description by rememberSaveable { mutableStateOf(film?.description ?: "") }
    var posterPath by rememberSaveable { mutableStateOf(film?.posterPath ?: "") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val isEditing = film != null

    fun validateInt(value: String): Boolean {
        return value.isNotBlank() && value.toIntOrNull() != null
    }

    fun validate(): Boolean {
        return title.isNotBlank() && director.isNotBlank() && description.isNotBlank() && validateInt(
            year
        ) && validateInt(duration)
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
                    onSuccess = { navigateBack() },
                    onError = { errorMessage = it })
            } else {
                viewModel.createFilm(filmCreate,
                    onSuccess = { navigateBack() },
                    onError = { errorMessage = it })
            }
        } else {
            errorMessage = "Todos los campos son obligatorios excepto el póster."
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Editar película"
                        else "Crear Película"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = ::onSubmitForm, modifier = Modifier.padding(16.dp)
            ) {
                if (isEditing) Icon(Icons.Default.Edit, contentDescription = "Editar")
                else Icon(Icons.Default.Check, contentDescription = "Crear")
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                OutlinedTextField(value = title,
                    onValueChange = { title = it },
                    modifier = Modifier
                        .width(600.dp)
                        .fillMaxSize(),
                    singleLine = true,
                    label = { Text("Título") })
                OutlinedTextField(value = director,
                    onValueChange = { director = it },
                    singleLine = true,
                    modifier = Modifier
                        .width(600.dp)
                        .fillMaxSize(),
                    label = { Text("Director") })
                OutlinedTextField(
                    value = year,
                    singleLine = true,
                    modifier = Modifier
                        .width(600.dp)
                        .fillMaxSize(),
                    onValueChange = { year = it },
                    label = { Text("Año") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = duration,
                    singleLine = true,
                    modifier = Modifier
                        .width(600.dp)
                        .fillMaxSize(),
                    onValueChange = { duration = it },
                    label = { Text("Duración (min)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .width(600.dp)
                        .fillMaxSize(),
                    label = { Text("Descripción") })
                OutlinedTextField(value = posterPath,
                    onValueChange = { posterPath = it },
                    modifier = Modifier
                        .width(600.dp)
                        .fillMaxSize(),
                    singleLine = true,
                    label = { Text("URL del Poster (opcional)") })

                errorMessage?.let { Text(it, color = Color.Red) }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditFilmFormPreview() {
    val viewModel = FilmViewModel(FilmRepository(ApiClient.retrofit.create(FilmService::class.java)))
    viewModel.setCurrentFilm(Film.example())
    FilmFormScreen(
        viewModel = viewModel
    ) {}
}

@Preview(showBackground = true)
@Composable
fun CreateFilmFormPreview() {
    FilmFormScreen(
        viewModel = FilmViewModel(FilmRepository(ApiClient.retrofit.create(FilmService::class.java)))
    ) {}
}
