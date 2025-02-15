package com.example.pruebacompose.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
object Loading

@Serializable
object Login

@Serializable
object Films

@Serializable
object FilmDetail

@Serializable
object CreateFilmForm

@Serializable
object EditFilmForm

@Serializable
object Profile

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Films", Films, Icons.Filled.Favorite),
    TopLevelRoute("Profile", Profile, Icons.Filled.Person)
)
