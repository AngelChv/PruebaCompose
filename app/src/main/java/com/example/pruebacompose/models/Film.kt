package com.example.pruebacompose.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// Utilizo @Serializable con json para pasar el objeto entre pantallas.
// No es lo mismo que utilizo con gson para pasar los datos a la api.
@Serializable
data class Film(
    val id: Int,
    val title: String,
    val director: String,
    val year: Int,
    val duration: Int,
    val description: String,
    val posterPath: String?,
)

fun jsonToFilm(json: String): Film {
    return Json.decodeFromString(json)
}