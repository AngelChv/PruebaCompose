package com.example.pruebacompose.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun CreateFilmFab(
    navigateToCreateFilm: () -> Unit,
) {
    FloatingActionButton(
        onClick = navigateToCreateFilm,
    ) {
        Icon(Icons.Default.Add, contentDescription = "Añadir Película")
    }
}